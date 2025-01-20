package dev.yidafu.blog.admin.views.pages.config

import dev.yidafu.blog.common.FormKeys
import dev.yidafu.blog.common.Routes
import dev.yidafu.blog.common.views.components.FormItem
import dev.yidafu.blog.common.views.components.formItem
import dev.yidafu.blog.common.vo.AdminSynchronousVO
import dev.yidafu.blog.i18n.AdminTxt
import io.github.allangomes.kotlinwind.css.I700
import io.github.allangomes.kotlinwind.css.LG
import io.github.allangomes.kotlinwind.css.kw
import kotlinx.html.*

class AdminConfigSyncPage(vo: AdminSynchronousVO) : AdminConfigBasePage<AdminSynchronousVO>(vo) {
  override fun getContent(vo: AdminSynchronousVO): DIV.() -> Unit =
    {
      h1 {
        +AdminTxt.sync.toString(vo.locale)
      }
      form {
        action = Routes.CONFIGURATION_URL
        method = FormMethod.post

        formItem(
          FormItem(FormKeys.CRON_EXPR, AdminTxt.cron_expr.toString(vo.locale), vo.cronExpr, InputType.text),
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
