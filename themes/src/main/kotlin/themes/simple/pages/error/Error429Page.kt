package dev.yidafu.blog.themes.simple.pages.error

import dev.yidafu.blog.i18n.AdminTxt
import dev.yidafu.blog.themes.CacheablePageProvider
import dev.yidafu.blog.themes.DataModal
import dev.yidafu.blog.themes.Page
import dev.yidafu.blog.themes.PageNames

class Error429Page(modal: DataModal) : ErrorPage(
  modal = modal,
  errorCode = 429,
  errorTitle = AdminTxt.error_429_title,
  errorDescription = AdminTxt.error_429_desc
)

class Error429PageProvider : CacheablePageProvider() {
  override fun getName(): String = PageNames.ERROR_429

  override fun createPage(modal: DataModal): Page = Error429Page(modal)
}

