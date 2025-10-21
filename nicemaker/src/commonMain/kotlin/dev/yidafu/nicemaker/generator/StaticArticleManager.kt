package dev.yidafu.nicemaker.generator

import com.eygraber.uri.Uri
import dev.yidafu.nicemaker.config.SiteConfig
import dev.yidafu.nicemaker.core.dto.CommonArticleDTO
import dev.yidafu.nicemaker.engine.ArticleManager
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.io.Buffer
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem

private val logger = KotlinLogging.logger {}

/**
 * 静态生成器专用的 ArticleManager 实现
 * 不依赖数据库，只处理图片文件
 * KMP兼容版本 - 使用uri-kmp库
 */
class StaticArticleManager(private val config: SiteConfig) : ArticleManager {

  override suspend fun needUpdate(
    identifier: String,
    rawContent: String,
  ): Boolean {
    // 静态生成器总是更新所有文章
    return true
  }

  override fun processImage(img: Path): Uri {
    // 复制图片到输出目录
    val outputDir = Path(config.build.output + "/images")
    if (!SystemFileSystem.exists(outputDir)) {
      SystemFileSystem.createDirectories(outputDir)
    }
    val targetFile = Path(outputDir.toString() + "/" + img.name)
    // 使用source和sink进行文件复制
    SystemFileSystem.source(img).use { source ->
      SystemFileSystem.sink(targetFile).use { sink ->
        val buffer = Buffer()
        while (true) {
          buffer.clear()
          val bytesRead = source.readAtMostTo(buffer, 8192)
          if (bytesRead == -1L) break
          sink.write(buffer, bytesRead)
        }
      }
    }
    logger.debug { "Copied image: ${img.name}" }
    return Uri.parse("/images/${img.name}")  // 使用uri-kmp
  }

  override suspend fun saveArticle(dto: CommonArticleDTO) {
    // 静态生成器不需要保存到数据库
    // 文章数据只在内存中处理
  }
}

