package dev.yidafu.nicemaker.common.dto

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class FrontMatterDTO(
  val title: String,
  val cover: String,
  val description: String? = null,
  val tags: List<String> = emptyList(),
  val series: String? = null,
  val seriesOrder: Int = 0,
  @Transient
  var rawContent: String? = null,
)
