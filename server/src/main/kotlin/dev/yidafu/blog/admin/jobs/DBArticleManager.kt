package dev.yidafu.blog.admin.jobs

import dev.yidafu.blog.common.BlogConfig
import dev.yidafu.blog.common.Routes
import dev.yidafu.blog.common.dto.CommonArticleDTO
import dev.yidafu.blog.common.modal.ArticleModel
import dev.yidafu.blog.common.modal.ArticleStatus
import dev.yidafu.blog.common.services.ArticleService
import dev.yidafu.blog.dev.yidafu.blog.engine.ArticleManager
import dev.yidafu.blog.dev.yidafu.blog.engine.Logger
import org.koin.java.KoinJavaComponent.inject
import java.io.File
import java.net.URI
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

class DBArticleManager : ArticleManager {
  private val articleService: ArticleService by inject(ArticleService::class.java)
  private val logger: Logger by inject(Logger::class.java)

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
    articleService.saveArticle(modal)
  }
}
