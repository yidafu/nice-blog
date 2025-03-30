package dev.yidafu.blog.themes.simple.pages.admin.config

import dev.yidafu.blog.common.FormKeys
import dev.yidafu.blog.common.Routes
import dev.yidafu.blog.i18n.AdminTxt
import dev.yidafu.blog.themes.*
import dev.yidafu.blog.themes.simple.components.FormItem
import dev.yidafu.blog.themes.simple.components.RadioItem
import dev.yidafu.blog.themes.simple.components.formItem
import dev.yidafu.blog.themes.simple.components.radioItem
import io.github.allangomes.kotlinwind.css.I700
import io.github.allangomes.kotlinwind.css.LG
import io.github.allangomes.kotlinwind.css.kw
import kotlinx.html.*

class AdminConfigDataSourcePage(modal: DataModal) : AdminConfigPage(modal) {
  override fun DIV.createContent() {
    val vo = modal.dataSource
    h1 {
      +AdminTxt.data_source.toText()
    }
    form {
      action = Routes.CONFIGURATION_URL
      method = FormMethod.post

      radioItem(
        RadioItem(
          FormKeys.SOURCE_TYPE,
          AdminTxt.source_type.toText(),
          vo.sourceType,
          listOf(
            RadioItem.Option("git", "Git", "git"),
            RadioItem.Option("gitlab", "Gitlab", "gitlab"),
            RadioItem.Option("github", "GitHub", "github"),
          ),
        ),
      )

      formItem(
        FormItem(FormKeys.SOURCE_URL, AdminTxt.source_url.toText(), vo.sourceUrl, InputType.url),
      )

      formItem(
        FormItem(FormKeys.SOURCE_BRANCH, AdminTxt.source_branch.toText(), vo.sourceBranch, InputType.text),
      )

      formItem(
        FormItem(FormKeys.SOURCE_TOKEN, AdminTxt.source_token.toText(), vo.sourceToken, InputType.text),
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

class AdminConfigDataSourcePageProvider : CacheablePageProvider() {
  override fun getName(): String = PageNames.ADMIN_CONFIG_DATA_SOURCE_PAGE

  override fun createPage(modal: DataModal): Page {
    return AdminConfigDataSourcePage(modal)
  }
}
