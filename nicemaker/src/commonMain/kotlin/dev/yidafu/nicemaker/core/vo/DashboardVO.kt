package dev.yidafu.nicemaker.core.vo

import kotlinx.serialization.Serializable
@Serializable
data class DashboardVO(
  val articleCount: Long,
  val accessCount: Long,
)
