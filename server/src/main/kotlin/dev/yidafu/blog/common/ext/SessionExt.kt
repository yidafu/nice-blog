package dev.yidafu.blog.common.ext

import io.ktor.server.sessions.*
import kotlinx.serialization.Serializable

@Serializable
data class AdminSession(
  val username: String,
  val publicKey: String,
  val privateKey: String,
)

fun CurrentSession.updateUsername(value: String) {
  val session = this.get<AdminSession>()
  // 登录成功后，清空 RSA 密钥，只保留 username
  this.set(session?.copy(username = value, publicKey = "", privateKey = "")
    ?: AdminSession(username = value, publicKey = "", privateKey = ""))
}

