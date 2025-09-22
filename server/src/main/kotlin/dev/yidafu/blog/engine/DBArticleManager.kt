package dev.yidafu.blog.engine

import dev.yidafu.blog.common.BlogConfig
import dev.yidafu.blog.common.Routes
import dev.yidafu.blog.common.db.dao.ArticleEntity
import dev.yidafu.blog.common.db.dao.ArticleHistoryEntity
import dev.yidafu.blog.common.db.tables.ArticleTable
import dev.yidafu.blog.common.dto.CommonArticleDTO
import dev.yidafu.blog.common.modal.ArticleStatus
import dev.yidafu.blog.common.services.ExposedBaseService
import org.jetbrains.exposed.v1.core.eq
import org.koin.core.annotation.Single
import java.io.File
import java.io.FileInputStream
import java.net.URI
import java.nio.file.Files
import java.nio.file.Paths
import java.util.UUID

@Single
class DBArticleManager(
  private val logger: Logger,
  private val config: GitConfig,
) : ExposedBaseService(), ArticleManager {
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

  /**
   * 保存文章
   */
  override suspend fun saveArticle(articleDTO: CommonArticleDTO): Unit =
    runDB {
      val identifier =
        articleDTO.filename
          .replace(".md", ".html")
          .replace(".ipynb", ".html")

      val existingArticle = findArticleByName(identifier)
      val article = existingArticle?.
      apply { // 更新现有文章
        title = articleDTO.frontMatter?.title ?: ""
        summary = articleDTO.frontMatter?.description
        cover = articleDTO.frontMatter?.cover
        this.identifier = identifier
        html = articleDTO.html
        series = ""
        content = articleDTO.rawContext
        status = ArticleStatus.Candidate
        sourceType = articleDTO.sourceType
        hash = ""
      }
          ?: // 创建新文章
          ArticleEntity.new {
            title = articleDTO.frontMatter?.title ?: ""
            summary = articleDTO.frontMatter?.description
            cover = articleDTO.frontMatter?.cover
            this.identifier = identifier
            html = articleDTO.html
            series = ""
            content = articleDTO.rawContext
            status = ArticleStatus.Candidate
            sourceType = articleDTO.sourceType
            hash = ""
          }
      createLog(article)
    }

  private fun createLog(article: ArticleEntity) {
    ArticleHistoryEntity.new {
      rawContent = article.content ?: ""
      articleId = article.tId
      renderedContent = article.html ?: ""
    }
  }

  private  fun findArticleByName(name: String): ArticleEntity? {
    return ArticleEntity.find {
      (ArticleTable.identifier eq name)
    }.singleOrNull()
  }
}
