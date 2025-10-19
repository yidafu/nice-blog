package dev.yidafu.nicemaker.themes.blank

import dev.yidafu.nicemaker.themes.CacheablePageProvider
import dev.yidafu.nicemaker.themes.DataModal
import dev.yidafu.nicemaker.themes.Page
import dev.yidafu.nicemaker.themes.PageNames
import kotlinx.html.*

class BlankPage(override val modal: DataModal) : Page {
  override fun render(html: HTML) {
    html.apply {
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
    }
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
