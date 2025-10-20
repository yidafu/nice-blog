package dev.yidafu.nicemaker.core.vo

import dev.yidafu.nicemaker.util.serialization.LocalDateTimeSerializer
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
@Serializable
data class ArticleVO(
  var id: Int = 0,
  var title: String = "",
  var cover: String? = null,
  var identifier: String? = null,
  var series: String? = null,
  var status: Int? = null,
  var summary: String? = null,
  var content: String? = null,
  var html: String? = null,
  @Serializable(with = LocalDateTimeSerializer::class)
  var createdAt: LocalDateTime? = null,
  var updatedAt: LocalDateTime? = null,
)
