package dev.yidafu.blog.bean.vo

import de.comahe.i18n4k.Locale

open class AdminBaseVo(
  val locale: Locale,
  val currentPath: String,
  val siteTitle: String,
  val githubUrl: String,
) {

}
