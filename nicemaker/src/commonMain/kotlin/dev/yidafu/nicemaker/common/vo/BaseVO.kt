package dev.yidafu.nicemaker.common.vo

/**
 * 基础VO类 - 使用String代替java.util.Locale以支持KMP
 */
open class BaseVO(
  val locale: String,  // 语言标签，如 "zh-CN", "en-US"
  val currentPath: String,
  val siteTitle: String,
  val githubUrl: String,
) {
  companion object {
    val empty = BaseVO("zh-CN", "", "", "")
  }
}
