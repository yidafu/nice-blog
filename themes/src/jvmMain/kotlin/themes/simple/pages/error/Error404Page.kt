package dev.yidafu.nicemaker.theme.simple.pages.error

import dev.yidafu.nicemaker.i18n.AdminTxt
import dev.yidafu.nicemaker.theme.CacheablePageProvider
import dev.yidafu.nicemaker.theme.DataModal
import dev.yidafu.nicemaker.theme.Page
import dev.yidafu.nicemaker.theme.PageNames

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

