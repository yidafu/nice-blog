package dev.yidafu.blog.themes.simple.pages.admin.sync

import dev.yidafu.blog.common.modal.SyncTaskStatus
import dev.yidafu.blog.common.vo.AdminSyncTaskVO
import dev.yidafu.blog.themes.*
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.html.*
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class AdminSyncLogDetailPage(modal: DataModal) : AdminSyncPage(modal) {
  @OptIn(ExperimentalTime::class)
  override fun DIV.createContent() {
    val syncTask =
      modal.obj<AdminSyncTaskVO>("task")
        ?: AdminSyncTaskVO(
          id = 0,
          callbackUrl = "",
          uuid = "",
          status = SyncTaskStatus.Failed,
          createdAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
          logs = "",
        )
    div {
      classes = setOf("sync-log")
      pre {
        attributes["style"] = "text-wrap: auto;"
        +syncTask.logs
      }
    }
  }
}

class AdminSyncLogDetailPageProvider : CacheablePageProvider() {
  override fun getName(): String = PageNames.ADMIN_CONFIG_SYNC_LOG_DETAIL_PAGE

  override fun createPage(modal: DataModal): Page = AdminSyncLogDetailPage(modal)
}
