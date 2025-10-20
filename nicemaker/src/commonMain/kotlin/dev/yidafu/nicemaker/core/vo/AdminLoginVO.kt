package dev.yidafu.nicemaker.core.vo

import kotlinx.serialization.Serializable
@Serializable
class AdminLoginVO(
  val publicKey: String,
  val errorMessage: String? = null,
)
