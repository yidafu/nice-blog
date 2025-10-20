package dev.yidafu.nicemaker.common.vo

/**
 * 页面VO基类 - 使用String代替java.util.Locale以支持KMP
 */
open class PageVO(
  var baseVO: BaseVO = BaseVO.empty,
) {
  val locale: String  // 语言标签
    get() = baseVO.locale
  val currentPath: String
    get() = baseVO.currentPath
  val githubUrl: String
    get() = baseVO.githubUrl
  val siteTitle: String
    get() = baseVO.siteTitle
}
