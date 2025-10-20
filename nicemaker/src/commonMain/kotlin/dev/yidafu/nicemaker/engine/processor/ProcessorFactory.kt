package dev.yidafu.nicemaker.engine.processor

import dev.yidafu.nicemaker.engine.ArticleManager
import dev.yidafu.nicemaker.engine.BaseLogger

/**
 * 处理器工厂 - 提供平台特定的处理器
 *
 * JVM平台：提供NotebookProcessor, FeishuProcessor
 * Native平台：返回空列表
 */
expect object ProcessorFactory {
  /**
   * 获取平台特定的处理器列表
   *
   * @param articleManager 文章管理器
   * @param logger 日志记录器
   * @param feishuAppId 飞书AppId（可选）
   * @param feishuAppSecret 飞书AppSecret（可选）
   * @return 处理器列表
   */
  fun getPlatformProcessors(
    articleManager: ArticleManager,
    logger: BaseLogger,
    feishuAppId: String? = null,
    feishuAppSecret: String? = null
  ): List<IProcessor>
}

