package dev.yidafu.nicemaker.theme

import dev.yidafu.nicemaker.theme.simple.SimpleTemplateManager
import dev.yidafu.nicemaker.theme.blank.BlankTemplateManager
import io.github.oshai.kotlinlogging.KotlinLogging

private val logger = KotlinLogging.logger {}

/**
 * 主题自动注册
 * 当 themes 模块被加载时，自动注册所有主题
 */
object ThemesInit {
  init {
    registerAllThemes()
  }

  private fun registerAllThemes() {
    try {
      TemplateManagerLoader.register(SimpleTemplateManager())
      logger.info { "Registered theme: Simple Theme" }
    } catch (e: Exception) {
      logger.warn(e) { "Failed to register Simple Theme" }
    }

    try {
      TemplateManagerLoader.register(BlankTemplateManager())
      logger.info { "Registered theme: Blank Theme" }
    } catch (e: Exception) {
      logger.warn(e) { "Failed to register Blank Theme" }
    }
  }
}

