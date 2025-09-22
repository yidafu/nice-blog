package dev.yidafu.blog.common.db.tables

import dev.yidafu.blog.common.modal.SyncTaskStatus

/**
 * 同步任务表定义
 * 对应 SyncTaskModel 实体类
 */
object SyncTaskTable : BaseTable("b_sync_task") {
  val callbackUrl = varchar("callback_url", 255).nullable()
  val uuid = varchar("uuid", 255).nullable()

  // 枚举类型的处理
  val status = enumeration<SyncTaskStatus>("status").nullable()

  val logs = text("logs").default("") // 日志内容
  val forceSync = bool("force_sync").default(false) // 是否强制同步

  const val APPEND_LOG_TEXT = "APPEND_LOG_TEXT"
}
