package dev.yidafu.blog.common.vo

import java.util.*

open class BaseVO(
  val locale: Locale,
  val currentPath: String,
  val siteTitle: String,
  val githubUrl: String,
) {
  companion object {
    val empty = BaseVO(Locale.forLanguageTag("zh-CN"), "", "", "")
  }
}
