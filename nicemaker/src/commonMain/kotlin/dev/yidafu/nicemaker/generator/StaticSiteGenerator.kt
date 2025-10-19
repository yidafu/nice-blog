package dev.yidafu.nicemaker.generator

import io.github.oshai.kotlinlogging.KotlinLogging

private val logger = KotlinLogging.logger {}

/**
 * 静态站点生成器主类
 * 协调内容加载和页面生成
 */
class StaticSiteGenerator(
  private val config: SiteConfig,
) {
  fun build() {
    logger.info { "🚀 Starting static site generation..." }
    logger.info { "   Site: ${config.site.title}" }
    logger.info { "   Theme: ${config.theme.name}" }

    try {
      // 1. 加载内容
      val contentLoader = ContentLoader(config)
      val articles = contentLoader.loadAllArticles()

      // 2. 加载 AboutMe 内容
      config.aboutContent = contentLoader.loadAboutMe()

      // 3. 生成页面
      val pageGenerator = PageGenerator(articles, config)
      pageGenerator.generate()

      logger.info { "✅ Build completed successfully!" }
    } catch (e: Exception) {
      logger.error(e) { "❌ Build failed" }
      throw e
    }
  }
}

