package dev.yidafu.nicemaker.common.query

data class PageQuery(
  val page: Int = 1,
  val size: Int = 10,
) {
  val offset: Int = size * (page - 1)
}
