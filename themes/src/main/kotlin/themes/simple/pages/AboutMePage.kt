package dev.yidafu.blog.themes.simple.pages

import dev.yidafu.blog.themes.CacheablePageProvider
import dev.yidafu.blog.themes.DataModal
import dev.yidafu.blog.themes.Page
import dev.yidafu.blog.themes.PageNames

class AboutMePage(modal: DataModal) : SimplePage(modal) {
}

class AboutMePageProvider : CacheablePageProvider() {
  override fun getName(): String = PageNames.ABOUT_ME

  override fun createPage(modal: DataModal): Page {
    return AboutMePage(modal)
  }
}
