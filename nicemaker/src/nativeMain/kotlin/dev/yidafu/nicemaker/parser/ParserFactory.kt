package dev.yidafu.nicemaker.parser

import dev.yidafu.nicemaker.parser.Parser

import dev.yidafu.nicemaker.engine.ArticleManager

/**
 * Native平台的处理器工厂实现
 * 不提供JVM特定的处理器
 */
actual object ParserFactory {
  actual fun getPlatformParsers(
    articleManager: ArticleManager,
    feishuAppId: String?,
    feishuAppSecret: String?
  ): List<Parser> {
    // Native平台暂不支持NotebookProcessor和FeishuProcessor
    // 它们依赖JVM特定的库
    return emptyList()
  }
}

