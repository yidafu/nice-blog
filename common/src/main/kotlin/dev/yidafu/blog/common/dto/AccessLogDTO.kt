package dev.yidafu.blog.common.dto

import kotlinx.datetime.LocalDateTime

class AccessLogDTO(
  val id: Long? = null,
  val uid: String,
  val accessTime: LocalDateTime,
  val sourceUrl: String,
  val referrerUrl: String,
  val ip: String,
  val ua: String,
)
