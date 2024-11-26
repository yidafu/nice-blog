package dev.yidafu.blog.bean.vo

import java.util.*

class AdminSynchronousVO(
  val cronExpr: String,
  locale: Locale,
  currentPath: String,
  siteTitle: String,
  githubUrl: String,
) : AdminBaseVo(
  locale,
  currentPath,
  siteTitle,
  githubUrl
){
}
