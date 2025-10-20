package dev.yidafu.nicemaker.common.dto

import kotlinx.datetime.LocalDateTime

data class ArticleDTO(
  val title: String = "",
  var cover: String? = null,
  var identifier: String? = null,
  var seriesId: Long? = null,
  var status: Int? = null,
  var summary: String? = null,
  var content: String? = null,
  var html: String? = null,
  val updatedAt: LocalDateTime? = null,
)
