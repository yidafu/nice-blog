package dev.yidafu.nicemaker.common.vo

import kotlinx.serialization.Serializable

@Serializable
class AdminLoginVO(
  val publicKey: String,
  val errorMessage: String? = null,
)
