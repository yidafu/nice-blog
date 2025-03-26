package dev.yidafu.blog.common.vo

import java.time.LocalTime

class AdminArticleDetailVO(
  var id: Long = 0,
  var title: String = "",
  var cover: String? = null,
  var identifier: String? = null,
  var series: String? = null,
  var status: Int? = null,
  var summary: String? = null,
  var content: String? = null,
  var html: String? = null,
  val updatedAt: LocalTime? = null,
) : PageVO()
