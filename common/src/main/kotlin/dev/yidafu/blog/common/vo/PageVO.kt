package dev.yidafu.blog.common.vo

import java.util.*

open class PageVO(
  var baseVO: BaseVO = BaseVO.empty,
) {
  val locale: Locale
    get() = baseVO.locale
  val currentPath: String
    get() = baseVO.currentPath
  val githubUrl: String
    get() = baseVO.githubUrl
  val siteTitle: String
    get() = baseVO.siteTitle
}
