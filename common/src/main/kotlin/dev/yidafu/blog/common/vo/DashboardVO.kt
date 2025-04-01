package dev.yidafu.blog.common.vo

import kotlinx.serialization.Serializable

@Serializable
data class DashboardVO(
  val articleCount: Long,
  val accessCount: Long,
)
