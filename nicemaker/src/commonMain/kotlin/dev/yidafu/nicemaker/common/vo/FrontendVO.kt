package dev.yidafu.nicemaker.common.vo

/**
 * 前端VO类 - 使用String代替java.util.Locale以支持KMP
 */
open class FrontendVO(
  val locale: String,  // 语言标签，如 "zh-CN", "en-US"
  val currentPath: String,
  val siteTitle: String,
  val githubUrl: String,
)
