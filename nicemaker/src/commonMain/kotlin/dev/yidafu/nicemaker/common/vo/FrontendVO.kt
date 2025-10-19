package dev.yidafu.nicemaker.common.vo

import java.util.*

open class FrontendVO(
  val locale: Locale,
  val currentPath: String,
  val siteTitle: String,
  val githubUrl: String,
)
