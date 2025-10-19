package dev.yidafu.blog.themes.simple.pages.error

import dev.yidafu.blog.i18n.AdminTxt
import dev.yidafu.blog.themes.CacheablePageProvider
import dev.yidafu.blog.themes.DataModal
import dev.yidafu.blog.themes.Page
import dev.yidafu.blog.themes.PageNames

class Error504Page(modal: DataModal) : ErrorPage(
  modal = modal,
  errorCode = 504,
  errorTitle = AdminTxt.error_504_title,
  errorDescription = AdminTxt.error_504_desc
)

class Error504PageProvider : CacheablePageProvider() {
  override fun getName(): String = PageNames.ERROR_504

  override fun createPage(modal: DataModal): Page = Error504Page(modal)
}

