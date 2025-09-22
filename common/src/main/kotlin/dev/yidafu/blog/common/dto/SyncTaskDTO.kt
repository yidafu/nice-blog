package dev.yidafu.blog.common.dto

import dev.yidafu.blog.common.modal.SyncTaskStatus
import kotlinx.datetime.LocalDateTime

data class SyncTaskDTO(
  val id: Int,
  val callbackUrl: String,
  val uuid: String,
  val status: SyncTaskStatus,
  val createdAt: LocalDateTime,
  val logs: String,
)
