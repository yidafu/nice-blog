package dev.yidafu.blog.common.vo

import dev.yidafu.blog.common.json.LocalDateTimeSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class ArticleVO(
  var id: Long = 0,
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
  @Serializable(with = LocalDateTimeSerializer::class)
  var updatedAt: LocalDateTime? = null,
)
