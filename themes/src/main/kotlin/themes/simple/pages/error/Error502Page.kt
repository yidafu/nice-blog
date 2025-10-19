package dev.yidafu.blog.themes.simple.pages.error

import dev.yidafu.blog.i18n.AdminTxt
import dev.yidafu.blog.themes.CacheablePageProvider
import dev.yidafu.blog.themes.DataModal
import dev.yidafu.blog.themes.Page
import dev.yidafu.blog.themes.PageNames

class Error502Page(modal: DataModal) : ErrorPage(
  modal = modal,
  errorCode = 502,
  errorTitle = AdminTxt.error_502_title,
  errorDescription = AdminTxt.error_502_desc
)

class Error502PageProvider : CacheablePageProvider() {
  override fun getName(): String = PageNames.ERROR_502

  override fun createPage(modal: DataModal): Page = Error502Page(modal)
}

