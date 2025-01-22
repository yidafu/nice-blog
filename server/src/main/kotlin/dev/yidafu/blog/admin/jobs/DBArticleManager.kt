package dev.yidafu.blog.admin.jobs

import dev.yidafu.blog.common.BlogConfig
import dev.yidafu.blog.common.Routes
import dev.yidafu.blog.common.converter.ArticleConvertor
import dev.yidafu.blog.common.dao.tables.records.BArticleRecord
import dev.yidafu.blog.common.dao.tables.references.B_ARTICLE
import dev.yidafu.blog.common.dto.CommonArticleDTO
import dev.yidafu.blog.common.modal.ArticleModel
import dev.yidafu.blog.common.modal.ArticleStatus
import dev.yidafu.blog.common.services.BaseService
import dev.yidafu.blog.dev.yidafu.blog.engine.ArticleManager
import org.jooq.CloseableDSLContext
import org.mapstruct.factory.Mappers
import java.io.File
import java.net.URI
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

class DBArticleManager(
  private val context: CloseableDSLContext,
) : ArticleManager, BaseService(context) {
  override suspend fun needUpdate(
    identifier: String,
    rawContent: String,
  ): Boolean {
    return true
  }

  override fun processImage(file: File): URI {
    val directory = File(BlogConfig.DEFAULT_UPLOAD_DIRECTORY)

    if (!directory.exists()) {
      directory.mkdirs()
    }
    val newFilename = UUID.randomUUID().toString() + "." + file.extension
    val newFilePath = Paths.get(directory.path, newFilename)
//    logger.log("copy file ${file.toPath()} to $newFilePath")
    Files.copy(file.toPath(), newFilePath)
    return URI.create(Routes.UPLOAD_URL.replace("*", newFilename))
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
    if (oldArticle == null) {
      // insert new article
      createArticle(article)
    } else {
      // update article
      article.id = oldArticle.id
      updateArticle(article)
    }

    return true
  }

  private suspend fun createArticle(article: ArticleModel) {
    val newArticleRecord: BArticleRecord = context.newRecord(B_ARTICLE)
    articleConvertor.mapToRecord(article, newArticleRecord)
    newArticleRecord.store()
  }

  private suspend fun updateArticle(article: ArticleModel) {
    val oldRecord: BArticleRecord? = context.selectFrom(B_ARTICLE).where(B_ARTICLE.ID.eq(article.id)).fetchOne()
    articleConvertor.mapToRecord(article, oldRecord)
    oldRecord?.store()
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
