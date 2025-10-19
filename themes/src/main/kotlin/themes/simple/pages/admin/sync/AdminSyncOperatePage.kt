package dev.yidafu.blog.themes.simple.pages.admin.sync

import dev.yidafu.blog.common.Routes
import dev.yidafu.blog.i18n.AdminTxt
import dev.yidafu.blog.themes.CacheablePageProvider
import dev.yidafu.blog.themes.DataModal
import dev.yidafu.blog.themes.Page
import dev.yidafu.blog.themes.PageNames
import kotlinx.html.*

class AdminSyncOperatePage(modal: DataModal) : AdminSyncPage(modal) {
  override fun DIV.createContent() {
    div {
      button {
        classes = setOf("btn", "btn--primary", "px-4", "py-3", "rounded-lg", "mr-6")
        attributes["hx-get"] = Routes.SYNC_API_START_URL
        attributes["hx-target"] = "#log-output"
        +AdminTxt.sync_start.toText()
      }

      button {
        classes = setOf("btn", "btn--primary", "px-4", "py-3", "rounded-lg")
        attributes["hx-get"] = Routes.SYNC_API_START_URL + "?force=1"
        attributes["hx-target"] = "#log-output"
        +AdminTxt.sync_force_start.toText()
      }

      div {
        classes = setOf("sync-log__output")
        id = "log-output"
      }
    }
  }
}

class AdminSyncOperatePageProvider : CacheablePageProvider() {
  override fun getName(): String = PageNames.ADMIN_CONFIG_SYNC_LOG_OPERATE_PAGE

  override fun createPage(modal: DataModal): Page {
    return AdminSyncOperatePage(modal)
  }
}
