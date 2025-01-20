package dev.yidafu.blog.common.vo

class AdminDataSourceVO(
  val sourceType: String,
  val sourceUrl: String,
  val sourceToken: String,
  val sourceBranch: String,
) : PageVO()
