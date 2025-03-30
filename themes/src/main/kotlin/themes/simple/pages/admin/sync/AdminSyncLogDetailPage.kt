package dev.yidafu.blog.themes.simple.pages.admin.sync

import dev.yidafu.blog.themes.*
import dev.yidafu.blog.themes.syncTask
import io.github.allangomes.kotlinwind.css.I50
import io.github.allangomes.kotlinwind.css.kw
import kotlinx.html.DIV
import kotlinx.html.div
import kotlinx.html.pre
import kotlinx.html.style

class AdminSyncLogDetailPage(modal: DataModal) : AdminSyncPage(modal) {
  override fun DIV.createContent() {
    val syncTask = modal.syncTask
    div {
      style =
        kw.inline {
          padding[2]
          border.rounded[8]
          background.gray[I50]
        }
      pre {
        style = "text-wrap: auto;"
        +syncTask.logs
      }
    }
  }
}

class AdminSyncLogDetailPageProvider : CacheablePageProvider() {
  override fun getName(): String = PageNames.ADMIN_CONFIG_SYNC_LOG_DETAIL_PAGE

  override fun createPage(modal: DataModal): Page = AdminSyncLogDetailPage(modal)
}
