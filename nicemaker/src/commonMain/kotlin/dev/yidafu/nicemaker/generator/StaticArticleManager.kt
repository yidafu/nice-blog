package dev.yidafu.nicemaker.generator

import dev.yidafu.nicemaker.common.dto.CommonArticleDTO
import dev.yidafu.nicemaker.engine.ArticleManager
import io.github.oshai.kotlinlogging.KotlinLogging
import java.io.File
import java.net.URI

private val logger = KotlinLogging.logger {}

/**
 * 静态生成器专用的 ArticleManager 实现
 * 不依赖数据库，只处理图片文件
 */
class StaticArticleManager(private val config: SiteConfig) : ArticleManager {

  override suspend fun needUpdate(
    identifier: String,
    rawContent: String,
  ): Boolean {
    // 静态生成器总是更新所有文章
    return true
  }

  override fun processImage(img: File): URI {
    // 复制图片到输出目录
    val outputDir = File(config.build.output, "images")
    outputDir.mkdirs()
    val targetFile = File(outputDir, img.name)
    img.copyTo(targetFile, overwrite = true)
    logger.debug { "Copied image: ${img.name}" }
    return URI("/images/${img.name}")
  }

  override suspend fun saveArticle(dto: CommonArticleDTO) {
    // 静态生成器不需要保存到数据库
    // 文章数据只在内存中处理
  }
}

