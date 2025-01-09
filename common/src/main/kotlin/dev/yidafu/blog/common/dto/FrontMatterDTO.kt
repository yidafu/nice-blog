package dev.yidafu.blog.common.dto

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient


@Serializable
data class FrontMatterDTO(
  val title: String,
  val cover: String,
  val description: String? = null,

  @Transient
  var rawContent: String? = null
//  val tags: List<String> = emptyList(),
)
