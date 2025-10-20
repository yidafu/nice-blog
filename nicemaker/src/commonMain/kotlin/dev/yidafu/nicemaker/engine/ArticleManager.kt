package dev.yidafu.nicemaker.engine

import com.eygraber.uri.Uri
import dev.yidafu.nicemaker.core.dto.CommonArticleDTO
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.writeString

/**
 * 文章管理器接口 - KMP兼容版本
 * 使用uri-kmp库实现跨平台URI支持
 */
interface ArticleManager {
  suspend fun needUpdate(
    identifier: String,
    rawContent: String,
  ): Boolean

  fun processImage(file: Path): Uri  // 使用uri-kmp的Uri

  suspend fun saveArticle(articleDTO: CommonArticleDTO)
}

open class DefaultArticleManager : ArticleManager {
  override suspend fun needUpdate(
    identifier: String,
    rawContent: String,
  ): Boolean {
    return true
  }

  override fun processImage(file: Path): Uri {
    return Uri.parse("file://$file")
  }

  override suspend fun saveArticle(articleDTO: CommonArticleDTO) {
    val outputPath = Path(articleDTO.filename + ".html")
    SystemFileSystem.sink(outputPath).buffered().use {
      it.writeString(articleDTO.html)
    }
  }
}

