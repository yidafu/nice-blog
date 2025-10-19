package dev.yidafu.nicemaker.generator

import dev.yidafu.nicemaker.engine.BaseLogger
import dev.yidafu.nicemaker.engine.GitConfig
import io.github.oshai.kotlinlogging.KotlinLogging

private val logger = KotlinLogging.logger {}

/**
 * 静态生成器专用的 Logger 实现
 * 使用 kotlin-logging
 */
class StaticLogger : BaseLogger(GitConfig("", "", "", "static-generator", false)) {

  override suspend fun log(str: String) {
    logger.debug { str }
  }

  override fun logSync(str: String) {
    logger.info { str }
  }
}

