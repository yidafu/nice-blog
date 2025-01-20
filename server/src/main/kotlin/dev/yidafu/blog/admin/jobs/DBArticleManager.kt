package dev.yidafu.blog.admin.jobs

import dev.yidafu.blog.common.dto.CommonArticleDTO
import dev.yidafu.blog.common.modal.ArticleModel
import dev.yidafu.blog.common.modal.ArticleStatus
import dev.yidafu.blog.common.services.ArticleService
import dev.yidafu.blog.dev.yidafu.blog.engine.ArticleManager
import org.koin.java.KoinJavaComponent.inject
import java.io.File
import java.net.URI

class DBArticleManager : ArticleManager {
  private val articleService: ArticleService by inject(ArticleService::class.java)

  override suspend fun needUpdate(
    identifier: String,
    rawContent: String,
  ): Boolean {
    return true
  }

  override suspend fun processImage(file: File): URI {
    return file.toURI()
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
