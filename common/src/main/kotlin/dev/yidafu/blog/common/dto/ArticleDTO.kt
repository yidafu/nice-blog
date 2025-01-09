package dev.yidafu.blog.common.dto

import java.time.LocalTime

class ArticleDTO(
  val title: String = "",
  var cover: String? = null,
  var identifier: String? = null,
  var seriesId: Long? = null,
  var status: Int? = null,
  var summary: String? = null,
  var content: String? = null,
  var html: String? = null,
  val updatedAt: LocalTime? = null,
)
