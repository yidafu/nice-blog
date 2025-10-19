package dev.yidafu.nicemaker.common.vo

import dev.yidafu.nicemaker.common.json.LocalDateTimeSerializer
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
class AdminArticleDetailVO(
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
  val updatedAt: LocalDateTime? = null,
)
