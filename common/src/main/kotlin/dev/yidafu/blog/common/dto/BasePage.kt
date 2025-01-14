package dev.yidafu.blog.common.dto

open class BasePage<T>(
  val page: Int = 1,
  val size: Int = 10,
  val total: Int = 0,
  val list: List<T> = emptyList(),
)
