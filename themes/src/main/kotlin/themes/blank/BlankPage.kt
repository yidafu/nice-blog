package dev.yidafu.blog.themes.blank

import dev.yidafu.blog.themes.CacheablePageProvider
import dev.yidafu.blog.themes.DataModal
import dev.yidafu.blog.themes.Page
import dev.yidafu.blog.themes.PageNames
import kotlinx.html.a
import kotlinx.html.body
import kotlinx.html.head
import kotlinx.html.stream.createHTML
import kotlinx.html.title

class BlankPage(override val modal: DataModal) : Page {
  override fun createPageHtml(): String {
    return createHTML().apply {
      head {
        title {
          +modal.siteTitle
        }
      }
      body {
        a {
          href = modal.path
          +modal.siteTitle
        }
      }
    }.finalize()
  }
}


class ArticleListPageProvider : CacheablePageProvider() {
  override fun getName(): String = PageNames.ARTICLE_LIST

  override fun createPage(modal: DataModal): Page {
    return BlankPage(modal)
  }
}


class ArticleDetailPageProvider : CacheablePageProvider() {
  override fun getName(): String = PageNames.ARTICLE_DETAIL

  override fun createPage(modal: DataModal): Page {
    return BlankPage(modal)
  }
}


class AboutMePageProvider : CacheablePageProvider() {
  override fun getName(): String = PageNames.ABOUT_ME

  override fun createPage(modal: DataModal): Page {
    return BlankPage(modal)
  }
}

