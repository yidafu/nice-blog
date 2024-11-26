package dev.yidafu.blog.bean.vo

import java.util.*

class AdminDataSourceVO(
  val sourceType: String,
  val sourceUrl: String,
  val sourceToken: String,
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
