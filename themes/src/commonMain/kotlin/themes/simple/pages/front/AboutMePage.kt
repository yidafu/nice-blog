package dev.yidafu.nicemaker.themes.simple.pages.front

import dev.yidafu.nicemaker.themes.CacheablePageProvider
import dev.yidafu.nicemaker.themes.DataModal
import dev.yidafu.nicemaker.themes.Page
import dev.yidafu.nicemaker.themes.PageNames
import kotlinx.html.*

class AboutMePage(modal: DataModal) : FrontPage(modal) {
  private val aboutContent: String = modal.str("aboutContent")

  override fun MAIN.createContent() {
    div {
      classes = setOf("container", "about-me")

      if (aboutContent.isEmpty()) {
        div {
          classes = setOf("empty-message")

          h1 { +"关于我" }
          p { +"暂无内容" }
          p {
            small {
              +"请在 Git 仓库根目录创建 "
              code { +"AboutMe.md" }
              +" 文件"
            }
          }
        }
      } else {
        div {
          classes = setOf("about-content")
          unsafe {
            +aboutContent
          }
        }
      }
    }
  }
}

class AboutMePageProvider : CacheablePageProvider() {
  override fun getName(): String = PageNames.ABOUT_ME

  override fun createPage(modal: DataModal): Page {
    return AboutMePage(modal)
  }
}
