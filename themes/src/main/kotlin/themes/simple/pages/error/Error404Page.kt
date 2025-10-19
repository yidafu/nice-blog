package dev.yidafu.blog.themes.simple.pages.error

import dev.yidafu.blog.i18n.AdminTxt
import dev.yidafu.blog.themes.CacheablePageProvider
import dev.yidafu.blog.themes.DataModal
import dev.yidafu.blog.themes.Page
import dev.yidafu.blog.themes.PageNames

class Error404Page(modal: DataModal) : ErrorPage(
  modal = modal,
  errorCode = 404,
  errorTitle = AdminTxt.error_404_title,
  errorDescription = AdminTxt.error_404_desc
)

class Error404PageProvider : CacheablePageProvider() {
  override fun getName(): String = PageNames.ERROR_404

  override fun createPage(modal: DataModal): Page = Error404Page(modal)
}

