package dev.yidafu.nicemaker.theme.simple.pages.front

import dev.yidafu.nicemaker.theme.CacheablePageProvider
import dev.yidafu.nicemaker.theme.DataModal
import dev.yidafu.nicemaker.theme.Page
import dev.yidafu.nicemaker.theme.PageNames
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
