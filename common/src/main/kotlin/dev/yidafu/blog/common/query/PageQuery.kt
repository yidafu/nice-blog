package dev.yidafu.blog.common.query

data class PageQuery(
  val page: Int = 1,
  val size: Int = 10,
)
