package dev.yidafu.nicemaker.parser

import dev.yidafu.nicemaker.engine.ArticleManager

/**
 * JVM平台的解析器工厂实现
 * FeishuParser 和 NotebookParser 已迁移至 commonMain，成为所有平台通用
 */
actual object ParserFactory {
  actual fun getPlatformParsers(
    articleManager: ArticleManager,
    feishuAppId: String?,
    feishuAppSecret: String?
  ): List<Parser> {
    // 所有解析器都已迁移至 commonMain，JVM 平台暂无特定解析器
    return emptyList()
  }
}
