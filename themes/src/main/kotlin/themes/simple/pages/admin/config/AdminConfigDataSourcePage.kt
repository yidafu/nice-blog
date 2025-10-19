package dev.yidafu.blog.themes.simple.pages.admin.config

import dev.yidafu.blog.common.FormKeys
import dev.yidafu.blog.common.Routes
import dev.yidafu.blog.common.vo.AdminDataSourceVO
import dev.yidafu.blog.i18n.AdminTxt
import dev.yidafu.blog.themes.*
import dev.yidafu.blog.themes.simple.components.FormItem
import dev.yidafu.blog.themes.simple.components.RadioItem
import dev.yidafu.blog.themes.simple.components.formItem
import dev.yidafu.blog.themes.simple.components.radioItem
import kotlinx.html.*

class AdminConfigDataSourcePage(modal: DataModal) : AdminConfigPage(modal) {
  override fun DIV.createContent() {
    val vo = modal.obj<AdminDataSourceVO>("config") ?: AdminDataSourceVO("", "", "", "", "", "")
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

      // 飞书配置分隔符
      div {
        classes = setOf("divider", "my-4")
      }

      h2 {
        classes = setOf("text-xl", "font-bold", "mb-4")
        +AdminTxt.feishu_config.toText()
      }

      formItem(
        FormItem(FormKeys.FEISHU_APP_ID, AdminTxt.feishu_app_id.toText(), vo.feishuAppId, InputType.text),
      )

      formItem(
        FormItem(FormKeys.FEISHU_APP_SECRET, AdminTxt.feishu_app_secret.toText(), vo.feishuAppSecret, InputType.password),
      )

      button {
        classes = setOf("btn", "btn--primary", "text-white", "text-center", "rounded-lg", "px-5", "py-2")
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
