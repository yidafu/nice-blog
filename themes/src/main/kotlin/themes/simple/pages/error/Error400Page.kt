package dev.yidafu.blog.themes.simple.pages.error

import dev.yidafu.blog.i18n.AdminTxt
import dev.yidafu.blog.themes.CacheablePageProvider
import dev.yidafu.blog.themes.DataModal
import dev.yidafu.blog.themes.Page
import dev.yidafu.blog.themes.PageNames

class Error400Page(modal: DataModal) : ErrorPage(
  modal = modal,
  errorCode = 400,
  errorTitle = AdminTxt.error_400_title,
  errorDescription = AdminTxt.error_400_desc
)

class Error400PageProvider : CacheablePageProvider() {
  override fun getName(): String = PageNames.ERROR_400

  override fun createPage(modal: DataModal): Page = Error400Page(modal)
}

