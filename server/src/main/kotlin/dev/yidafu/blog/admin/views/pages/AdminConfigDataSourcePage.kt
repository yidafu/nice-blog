package dev.yidafu.blog.admin.views.pages

import dev.yidafu.blog.common.bean.vo.AdminDataSourceVO
import dev.yidafu.blog.common.bean.vo.AdminSynchronousVO
import dev.yidafu.blog.common.FormKeys
import dev.yidafu.blog.common.Routes
import dev.yidafu.blog.i18n.AdminTxt
import dev.yidafu.blog.common.views.components.FormItem
import dev.yidafu.blog.common.views.components.RadioItem
import dev.yidafu.blog.common.views.components.formItem
import dev.yidafu.blog.common.views.components.radioItem
import io.github.allangomes.kotlinwind.css.I700
import io.github.allangomes.kotlinwind.css.LG
import io.github.allangomes.kotlinwind.css.kw
import kotlinx.html.*

class AdminConfigDataSourcePage(private val syncVo: AdminDataSourceVO) : AdminConfigBasePage(syncVo) {

  override fun getContent() {
      DIV(mapOf(), tagConsumer).visit {
        h1 {
          + AdminTxt.data_source.toString(vo.locale)
        }
        form {
          action = Routes.CONFIGURATION_URL
          method = FormMethod.post

          radioItem(
            RadioItem(FormKeys.SOURCE_TYPE, AdminTxt.source_type.toString(vo.locale),syncVo.sourceType, listOf(
              RadioItem.Option("git", "Git", "git"),
              RadioItem.Option("gitlab", "Gitlab", "gitlab"),
              RadioItem.Option("github", "GitHub", "github"),
            ))
          )

          formItem(
            FormItem(FormKeys.SOURCE_URL, AdminTxt.source_url.toString(syncVo.locale), syncVo.sourceUrl, InputType.url)
          )
          formItem(
            FormItem(FormKeys.SOURCE_TOKEN, AdminTxt.source_token.toString(syncVo.locale), syncVo.sourceToken, InputType.text)
          )

          button {
            style = kw.inline { text.white.center; background.blue[I700]; border.rounded[LG]; padding.x[5].y[2] }
            + AdminTxt.submit.toString()
          }
        }
      }
  }
}
