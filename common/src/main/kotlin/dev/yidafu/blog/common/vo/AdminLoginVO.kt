package dev.yidafu.blog.common.vo

class AdminLoginVO(
  val publicKey: String,
  val errorMessage: String? = null,
) : PageVO()
