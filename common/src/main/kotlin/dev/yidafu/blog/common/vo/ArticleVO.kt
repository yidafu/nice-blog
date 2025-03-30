package dev.yidafu.blog.common.vo

import dev.yidafu.blog.common.json.LocalDateSerializer
import dev.yidafu.blog.common.json.LocalTimeSerializer
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.LocalTime

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
  @Serializable(with = LocalDateSerializer::class)
  val updatedAt: LocalDate? = null,
)
