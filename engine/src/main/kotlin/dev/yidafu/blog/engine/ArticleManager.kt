package dev.yidafu.blog.engine

import dev.yidafu.blog.common.dto.CommonArticleDTO
import dev.yidafu.blog.engine.TaskScope.Companion.NAME
import org.koin.core.annotation.Scope
import org.koin.core.annotation.Scoped
import java.io.File
import java.net.URI

interface ArticleManager {
  suspend fun needUpdate(
    identifier: String,
    rawContent: String,
  ): Boolean

  fun processImage(file: File): URI

  suspend fun saveArticle(articleDTO: CommonArticleDTO)
}

@Scope(name = NAME)
@Scoped
open class DefaultArticleManager : ArticleManager {
  override suspend fun needUpdate(
    identifier: String,
    rawContent: String,
  ): Boolean {
    return true
  }

  override fun processImage(file: File): URI {
    return file.toURI()
  }

  override suspend fun saveArticle(articleDTO: CommonArticleDTO) {
    File(articleDTO.filename + ".html").writeText(articleDTO.html)
  }
}
