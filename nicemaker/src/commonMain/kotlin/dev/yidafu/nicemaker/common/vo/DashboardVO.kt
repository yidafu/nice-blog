package dev.yidafu.nicemaker.common.vo

import kotlinx.serialization.Serializable

@Serializable
data class DashboardVO(
  val articleCount: Long,
  val accessCount: Long,
)
