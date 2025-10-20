package dev.yidafu.nicemaker.engine.processor

import dev.yidafu.nicemaker.engine.ArticleManager
import dev.yidafu.nicemaker.engine.BaseLogger

/**
 * Native平台的处理器工厂实现
 * 不提供JVM特定的处理器
 */
actual object ProcessorFactory {
  actual fun getPlatformProcessors(
    articleManager: ArticleManager,
    logger: BaseLogger,
    feishuAppId: String?,
    feishuAppSecret: String?
  ): List<IProcessor> {
    // Native平台暂不支持NotebookProcessor和FeishuProcessor
    // 它们依赖JVM特定的库
    return emptyList()
  }
}

