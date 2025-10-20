package dev.yidafu.nicemaker.theme.blank

import dev.yidafu.nicemaker.theme.CacheablePageProvider
import dev.yidafu.nicemaker.theme.DataModal
import dev.yidafu.nicemaker.theme.Page
import dev.yidafu.nicemaker.theme.PageNames
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
