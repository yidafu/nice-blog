package dev.yidafu.blog.dev.yidafu.blog.engine

import dev.yidafu.blog.common.dto.CommonArticleDTO
import java.io.File
import java.net.URI

interface ArticleManager {

  suspend fun needUpdate(identifier: String, rawContent: String): Boolean

  suspend fun processImage(file: File): URI

  suspend fun saveArticle(articleDTO: CommonArticleDTO)
}


open class DefaultArticleManager : ArticleManager{
  override suspend fun needUpdate(identifier: String, rawContent: String): Boolean {
    return true
  }

  override suspend fun processImage(file: File) : URI{
    return file.toURI()
  }

  override suspend fun saveArticle(articleDTO: CommonArticleDTO) {
    File(articleDTO.filename + ".html").writeText(articleDTO.html)
  }
}
