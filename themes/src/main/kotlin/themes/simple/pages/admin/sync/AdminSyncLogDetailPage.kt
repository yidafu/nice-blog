package dev.yidafu.blog.themes.simple.pages.admin.sync

import dev.yidafu.blog.themes.*
import dev.yidafu.blog.themes.syncTask
import io.github.allangomes.kotlinwind.css.I50
import io.github.allangomes.kotlinwind.css.kw
import kotlinx.html.*

class AdminSyncLogDetailPage(modal: DataModal) : AdminSyncPage(modal) {
  override fun DIV.createContent() {
    val syncTask = modal.syncTask
    div("log-content") {
      style =
        kw.inline {
          padding[4]
          border.rounded[8]
          width[180]
        }

        unsafe {
          + syncTask.logs
        }
    }
  }
}

class AdminSyncLogDetailPageProvider : CacheablePageProvider() {
  override fun getName(): String = PageNames.ADMIN_CONFIG_SYNC_LOG_DETAIL_PAGE

  override fun createPage(modal: DataModal): Page = AdminSyncLogDetailPage(modal)
}
