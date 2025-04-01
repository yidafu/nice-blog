package dev.yidafu.blog.common.dto

import java.time.LocalDateTime

class AccessLogDTO(
  val id: Long? = null,
  val uid: String,
  val accessTime: LocalDateTime,
  val sourceUrl: String,
  val referrerUrl: String,
  val ip: String,
  val ua: String,
)
