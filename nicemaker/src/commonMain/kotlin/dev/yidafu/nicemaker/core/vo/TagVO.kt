package dev.yidafu.nicemaker.core.vo

import kotlinx.serialization.Serializable
@Serializable
data class TagVO(
  val name: String,
  val count: Int,
  val slug: String = name.lowercase().replace(" ", "-"),
)
