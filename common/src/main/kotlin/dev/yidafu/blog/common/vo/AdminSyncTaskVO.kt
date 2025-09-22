package dev.yidafu.blog.common.vo

import dev.yidafu.blog.common.json.LocalDateTimeSerializer
import dev.yidafu.blog.common.modal.SyncTaskStatus
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class AdminSyncTaskVO(
  val id: Int,
  val callbackUrl: String,
  val uuid: String,
  val status: SyncTaskStatus,
  @Serializable(with = LocalDateTimeSerializer::class)
  val createdAt: LocalDateTime,
  val logs: String,
)
