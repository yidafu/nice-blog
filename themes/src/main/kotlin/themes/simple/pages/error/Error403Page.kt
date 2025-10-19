package dev.yidafu.blog.themes.simple.pages.error

import dev.yidafu.blog.i18n.AdminTxt
import dev.yidafu.blog.themes.CacheablePageProvider
import dev.yidafu.blog.themes.DataModal
import dev.yidafu.blog.themes.Page
import dev.yidafu.blog.themes.PageNames

class Error403Page(modal: DataModal) : ErrorPage(
  modal = modal,
  errorCode = 403,
  errorTitle = AdminTxt.error_403_title,
  errorDescription = AdminTxt.error_403_desc
)

class Error403PageProvider : CacheablePageProvider() {
  override fun getName(): String = PageNames.ERROR_403

  override fun createPage(modal: DataModal): Page = Error403Page(modal)
}

