package dev.yidafu.blog.common.dto

import dev.yidafu.blog.common.modal.SyncTaskStatus
import java.time.LocalDateTime

data class SyncTaskDTO(
  val id: Long,
  val callbackUrl: String,
  val uuid: String,
  val status: SyncTaskStatus,
  val createdAt: LocalDateTime,
  val logs: String,
)
