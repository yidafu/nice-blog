package dev.yidafu.nicemaker.parser

import dev.yidafu.nicemaker.engine.ArticleManager

/**
 * 解析器工厂 - 提供平台特定的解析器
 *
 * JVM平台：提供NotebookParser, FeishuParser
 * Native平台：返回空列表
 */
expect object ParserFactory {
  /**
   * 获取平台特定的解析器列表
   *
   * @param articleManager 文章管理器
   * @param feishuAppId 飞书AppId（可选）
   * @param feishuAppSecret 飞书AppSecret（可选）
   * @return 解析器列表
   */
  fun getPlatformParsers(
    articleManager: ArticleManager,
    feishuAppId: String? = null,
    feishuAppSecret: String? = null
  ): List<Parser>
}

