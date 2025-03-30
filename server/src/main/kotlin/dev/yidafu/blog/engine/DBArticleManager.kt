package dev.yidafu.blog.engine

import dev.whyoleg.cryptography.CryptographyProvider
import dev.whyoleg.cryptography.DelicateCryptographyApi
import dev.whyoleg.cryptography.algorithms.MD5
import dev.yidafu.blog.common.BlogConfig
import dev.yidafu.blog.common.Routes
import dev.yidafu.blog.common.converter.ArticleConvertor
import dev.yidafu.blog.common.dao.tables.records.BArticleRecord
import dev.yidafu.blog.common.dao.tables.references.B_ARTICLE
import dev.yidafu.blog.common.dao.tables.references.B_ARTICLE_HISTORY
import dev.yidafu.blog.common.dto.CommonArticleDTO
import dev.yidafu.blog.common.modal.ArticleModel
import dev.yidafu.blog.common.modal.ArticleStatus
import dev.yidafu.blog.common.services.BaseService
import org.jooq.CloseableDSLContext
import org.mapstruct.factory.Mappers
import java.io.File
import java.io.FileInputStream
import java.net.URI
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

class DBArticleManager(
  private val context: CloseableDSLContext,
  private val logger: Logger,
) : ArticleManager, BaseService(context) {
  override suspend fun needUpdate(
    identifier: String,
    rawContent: String,
  ): Boolean {
    return true
  }

  override fun processImage(file: File): URI {
    // if cover not exist, return default cover.
    if (!file.exists()) return URI.create("/static/default-cover.png")

    logger.logSync("[Image] upload image ${file.toPath()}")
    val directory = File(BlogConfig.DEFAULT_UPLOAD_DIRECTORY)

    if (!directory.exists()) {
      directory.mkdirs()
    }
    val newFilename = UUID.randomUUID().toString() + "." + file.extension
    val newFilePath = Paths.get(directory.path, newFilename)
//    logger.log("copy file ${file.toPath()} to $newFilePath")
    FileInputStream(file).use { stream ->
      Files.copy(stream, newFilePath)
    }
    return URI.create(Routes.UPLOAD_URL.replace("*", newFilename))
  }

  @OptIn(DelicateCryptographyApi::class, ExperimentalStdlibApi::class)
  private suspend fun hash(str: String): String {
    val md5 = CryptographyProvider.Default.get(MD5)
    return md5.hasher().hash(str.toByteArray()).toHexString()
  }

  override suspend fun saveArticle(dto: CommonArticleDTO) {
    val identifier =
      dto.filename
        .replace(".md", ".html")
        .replace(".ipynb", ".html")

    val modal =
      ArticleModel(
        title = dto.frontMatter?.title ?: "",
        summary = dto.frontMatter?.description,
        cover = dto.frontMatter?.cover,
        identifier = identifier,
        html = dto.html,
        series = "",
        content = dto.rawContext,
        status = ArticleStatus.Candidate,
        sourceType = dto.sourceType,
        hash = "",
      )
    modal.createdAt = dto.createTime
    modal.updatedAt = dto.updateTime
    saveArticle(modal)
  }

  private val articleConvertor = Mappers.getMapper(ArticleConvertor::class.java)

  suspend fun saveArticle(article: ArticleModel): Boolean {
    if (article.identifier == null) {
      return false
    }
    val oldArticle = findArticleByName(article.identifier!!)

    val md5Hash = hash(article.content ?: "")
    if (oldArticle?.hash == md5Hash) {
      return true
    }
    article.hash = md5Hash

    if (oldArticle == null) {
      // insert new article
      createArticle(article)
    } else {
      // update article
      article.id = oldArticle.id
      updateArticle(article)
    }

    createLog(article)
    return true
  }

  private suspend fun createLog(article: ArticleModel) {
    val history = context.newRecord(B_ARTICLE_HISTORY)
    history.rawContent = article.content
    history.articleId = article.id
    history.renderedContent = article.html
    history.store()
  }

  private suspend fun createArticle(article: ArticleModel) {
    logger.log("[Article] create article ${article.identifier}")
    val newArticleRecord: BArticleRecord = context.newRecord(B_ARTICLE)
    articleConvertor.mapToRecord(article, newArticleRecord)
    newArticleRecord.store()
  }

  private suspend fun updateArticle(article: ArticleModel) {
    logger.log("[Article] upload article ${article.identifier}")
    val oldRecord: BArticleRecord? = context.selectFrom(B_ARTICLE).where(B_ARTICLE.ID.eq(article.id)).fetchOne()
    articleConvertor.mapToRecord(article, oldRecord)
    oldRecord?.store()
    oldRecord?.refresh()
  }

  private suspend fun findArticleByName(name: String): ArticleModel? =
    runDB {
      val article: BArticleRecord? =
        context.selectFrom(B_ARTICLE).where(
          B_ARTICLE.IDENTIFIER.eq(name),
        ).fetchOne()
      articleConvertor.recordToModal(article)
    }
}
