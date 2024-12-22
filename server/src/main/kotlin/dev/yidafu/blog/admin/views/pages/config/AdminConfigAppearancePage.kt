package dev.yidafu.blog.admin.views.pages.config

import dev.yidafu.blog.common.vo.AdminAppearanceVO
import dev.yidafu.blog.common.FormKeys
import dev.yidafu.blog.common.Routes
import dev.yidafu.blog.i18n.AdminTxt
import dev.yidafu.blog.common.views.components.FormItem
import dev.yidafu.blog.common.views.components.formItem
import io.github.allangomes.kotlinwind.css.I700
import io.github.allangomes.kotlinwind.css.LG
import io.github.allangomes.kotlinwind.css.kw
import kotlinx.html.*

class AdminConfigAppearancePage(vo: AdminAppearanceVO) : AdminConfigBasePage<AdminAppearanceVO>(vo) {
  override fun getContent(vo: AdminAppearanceVO): DIV.() -> Unit = {
      h1 {
        +AdminTxt.appearance.toString(vo.locale)
      }
      form {
        action = Routes.CONFIGURATION_URL
        method = FormMethod.post
        formItem(FormItem(FormKeys.SITE_TITLE, AdminTxt.site_title.toString(vo.locale), vo.siteTitle, InputType.text))

        formItem(FormItem(FormKeys.GITHUB_URL, AdminTxt.github_url.toString(vo.locale), vo.githubUrl, InputType.url))

        button {
          style = kw.inline { text.white.center; background.blue[I700]; border.rounded[LG]; padding.x[5].y[2] }
          +AdminTxt.submit.toString()
        }
      }
    }

}
