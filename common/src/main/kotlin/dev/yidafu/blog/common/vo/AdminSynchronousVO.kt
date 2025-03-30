package dev.yidafu.blog.common.vo

import kotlinx.serialization.Serializable

@Serializable
class AdminSynchronousVO(
  val cronExpr: String,
)
