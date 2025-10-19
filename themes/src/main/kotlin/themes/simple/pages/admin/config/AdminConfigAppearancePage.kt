package dev.yidafu.blog.themes.simple.pages.admin.config

import dev.yidafu.blog.common.FormKeys
import dev.yidafu.blog.common.Routes
import dev.yidafu.blog.i18n.AdminTxt
import dev.yidafu.blog.themes.CacheablePageProvider
import dev.yidafu.blog.themes.DataModal
import dev.yidafu.blog.themes.Page
import dev.yidafu.blog.themes.PageNames
import dev.yidafu.blog.themes.simple.components.FormItem
import dev.yidafu.blog.themes.simple.components.formItem
import kotlinx.html.*

class AdminConfigAppearancePage(modal: DataModal) : AdminConfigPage(modal) {
  override fun DIV.createContent() {
    h1 {
      +AdminTxt.appearance.toText()
    }
    form {
      action = Routes.CONFIGURATION_URL
      method = FormMethod.post
      formItem(FormItem(FormKeys.SITE_TITLE, AdminTxt.site_title.toText(), siteTitle, InputType.text))

      formItem(FormItem(FormKeys.GITHUB_URL, AdminTxt.github_url.toText(), githubUrl, InputType.url))

      button {
        classes = setOf("btn", "btn--primary", "text-white", "text-center", "rounded-lg", "px-5", "py-2")
        +AdminTxt.submit.toString()
      }
    }
  }
}

class AdminConfigAppearancePageProvider : CacheablePageProvider() {
  override fun getName(): String = PageNames.ADMIN_CONFIG_APPEARANCE_PAGE

  override fun createPage(modal: DataModal): Page {
    return AdminConfigAppearancePage(modal)
  }
}
