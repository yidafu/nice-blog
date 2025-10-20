package dev.yidafu.nicemaker.engine.processor

import dev.yidafu.nicemaker.engine.ArticleManager
import dev.yidafu.nicemaker.engine.BaseLogger

/**
 * JVM平台的处理器工厂实现
 * 提供NotebookProcessor和FeishuProcessor
 */
actual object ProcessorFactory {
  actual fun getPlatformProcessors(
    articleManager: ArticleManager,
    logger: BaseLogger,
    feishuAppId: String?,
    feishuAppSecret: String?
  ): List<IProcessor> {
    val processors = mutableListOf<IProcessor>()

    // 添加Notebook处理器
    try {
      processors.add(NotebookProcessor(articleManager, logger))
    } catch (e: Exception) {
      logger.logSync("[ProcessorFactory] Failed to load NotebookProcessor: ${e.message}")
    }

    // 如果提供了飞书配置，添加飞书处理器
    if (feishuAppId != null && feishuAppSecret != null &&
        feishuAppId.isNotBlank() && feishuAppSecret.isNotBlank()) {
      try {
        processors.add(FeishuProcessor(articleManager, logger, feishuAppId, feishuAppSecret))
      } catch (e: Exception) {
        logger.logSync("[ProcessorFactory] Failed to load FeishuProcessor: ${e.message}")
      }
    }

    return processors
  }
}

