package dev.yidafu.blog.themes.simple.pages.admin.config

import dev.yidafu.blog.common.FormKeys
import dev.yidafu.blog.common.Routes
import dev.yidafu.blog.i18n.AdminTxt
import dev.yidafu.blog.themes.*
import dev.yidafu.blog.themes.simple.components.FormItem
import dev.yidafu.blog.themes.simple.components.formItem
import io.github.allangomes.kotlinwind.css.I700
import io.github.allangomes.kotlinwind.css.LG
import io.github.allangomes.kotlinwind.css.kw
import kotlinx.html.*

class AdminConfigSyncPage(modal: DataModal) : AdminConfigPage(modal) {
  val vo = modal.synchronousConfig

  override fun DIV.createContent() {
    h1 {
      +AdminTxt.sync.toText()
    }
    form {
      action = Routes.CONFIGURATION_URL
      method = FormMethod.post

      formItem(
        FormItem(FormKeys.CRON_EXPR, AdminTxt.cron_expr.toText(), vo.cronExpr, InputType.text),
      )

      button {
        style =
          kw.inline {
            text.white.center
            background.blue[I700]
            border.rounded[LG]
            padding.x[5].y[2]
          }
        +AdminTxt.submit.toString()
      }
    }
  }
}

class AdminConfigSyncPageProvider : CacheablePageProvider() {


  override fun getName(): String  = PageNames.ADMIN_CONFIG_SYNC_PAGE

  override fun createPage(modal: DataModal): Page {
    return AdminConfigSyncPage(modal)
  }
}
