package dev.yidafu.nicemaker.common.vo

import kotlinx.serialization.Serializable

@Serializable
data class TagVO(
  val name: String,
  val count: Int,
  val slug: String = name.lowercase().replace(" ", "-"),
)

