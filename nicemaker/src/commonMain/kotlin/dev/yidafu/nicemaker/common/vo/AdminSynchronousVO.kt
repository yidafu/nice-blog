package dev.yidafu.nicemaker.common.vo

import kotlinx.serialization.Serializable

@Serializable
class AdminSynchronousVO(
  val cronExpr: String,
)
