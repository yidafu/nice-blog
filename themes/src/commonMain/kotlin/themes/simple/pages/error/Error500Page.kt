package dev.yidafu.nicemaker.themes.simple.pages.error

import dev.yidafu.nicemaker.i18n.AdminTxt
import dev.yidafu.nicemaker.themes.CacheablePageProvider
import dev.yidafu.nicemaker.themes.DataModal
import dev.yidafu.nicemaker.themes.Page
import dev.yidafu.nicemaker.themes.PageNames

class Error500Page(modal: DataModal) : ErrorPage(
  modal = modal,
  errorCode = 500,
  errorTitle = AdminTxt.error_500_title,
  errorDescription = AdminTxt.error_500_desc
)

class Error500PageProvider : CacheablePageProvider() {
  override fun getName(): String = PageNames.ERROR_500

  override fun createPage(modal: DataModal): Page = Error500Page(modal)
}

