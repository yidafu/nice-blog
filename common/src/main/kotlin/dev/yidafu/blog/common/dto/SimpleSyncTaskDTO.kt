package dev.yidafu.blog.common.dto

import dev.yidafu.blog.common.modal.SyncTaskStatus
import java.time.LocalDateTime

data class SimpleSyncTaskDTO(
  val id: Long,
  val callbackUrl: String,
  val uuid: String,
  val status: SyncTaskStatus,
  val createdAt: LocalDateTime,
)
