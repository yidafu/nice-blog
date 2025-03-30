package dev.yidafu.blog.themes.simple.pages.admin.sync

import dev.yidafu.blog.common.Routes
import dev.yidafu.blog.themes.icons.SyncIcon
import dev.yidafu.blog.themes.icons.SyncLogIcon
import dev.yidafu.blog.i18n.AdminTxt
import dev.yidafu.blog.themes.DataModal
import dev.yidafu.blog.themes.simple.components.TabOption
import dev.yidafu.blog.themes.simple.components.tabs
import dev.yidafu.blog.themes.simple.pages.admin.AdminPage
import kotlinx.html.DIV

abstract class AdminSyncPage(modal: DataModal) : AdminPage(modal) {
  private fun getOptions() =
    listOf(
      TabOption(AdminTxt.sync_operate.toText(), Routes.SYNC_OPERATE_URL, SyncIcon(), Routes.SYNC_OPERATE_URL == currentPath),
      TabOption(AdminTxt.sync_log.toText(), Routes.SYNC_LOG_URL, SyncLogIcon(), Routes.SYNC_LOG_URL == currentPath),
    )

  abstract fun DIV.createContent(): Unit

  override fun DIV.layoutBlock() {
    tabs(options = getOptions()) {
      createContent()
    }
  }
}
