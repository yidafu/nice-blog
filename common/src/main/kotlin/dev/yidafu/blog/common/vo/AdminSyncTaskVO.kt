package dev.yidafu.blog.common.vo

import dev.yidafu.blog.common.json.LocalDateTimeSerializer
import dev.yidafu.blog.common.modal.SyncTaskStatus
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class SyncTaskVO(
  val id: Long,
  val callbackUrl: String,
  val uuid: String,
  val status: SyncTaskStatus,
  @Serializable(with = LocalDateTimeSerializer::class)
  val createdAt: LocalDateTime,
  val logs: String,
)
