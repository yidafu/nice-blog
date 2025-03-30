package dev.yidafu.blog.themes.simple.pages.front

import dev.yidafu.blog.themes.CacheablePageProvider
import dev.yidafu.blog.themes.DataModal
import dev.yidafu.blog.themes.Page
import dev.yidafu.blog.themes.PageNames
import kotlinx.html.MAIN
import kotlinx.html.h1

class AboutMePage(modal: DataModal) : FrontPage(modal) {
  override fun MAIN.createContent() {
    h1 { + "About Me" }
  }
}

class AboutMePageProvider : CacheablePageProvider() {
  override fun getName(): String = PageNames.ABOUT_ME

  override fun createPage(modal: DataModal): Page {
    return AboutMePage(modal)
  }
}
