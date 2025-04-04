package dev.yidafu.blog.common.vo

import kotlinx.serialization.Serializable

@Serializable
class AdminLoginVO(
  val publicKey: String,
  val errorMessage: String? = null,
  val redirectUrl: String? = null,
)
