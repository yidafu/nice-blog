package dev.yidafu.blog.themes.simple.pages.admin.sync

import dev.yidafu.blog.common.Routes
import dev.yidafu.blog.i18n.AdminTxt
import dev.yidafu.blog.themes.CacheablePageProvider
import dev.yidafu.blog.themes.DataModal
import dev.yidafu.blog.themes.Page
import dev.yidafu.blog.themes.PageNames
import io.github.allangomes.kotlinwind.css.I700
import io.github.allangomes.kotlinwind.css.LG
import io.github.allangomes.kotlinwind.css.kw
import kotlinx.html.*

class AdminSyncOperatePage(modal: DataModal) : AdminSyncPage(modal) {
  override fun DIV.createContent() {
    div {
      button {
        style =
          kw.inline {
            padding.x[4].y[3]
            border.rounded[LG]
            text.white
            background.blue[I700]
          }
        attributes["hx-get"] = Routes.SYNC_API_START_URL
        attributes["hx-target"] = "#log-output"
        +AdminTxt.sync_start.toText()
      }

      div {
        style =
          kw.inline {
            padding[4]
            border.rounded[LG]
          }
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
