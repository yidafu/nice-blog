package dev.yidafu.blog.themes.simple.pages.front

import dev.yidafu.blog.common.Routes
import dev.yidafu.blog.themes.simple.components.footerComponent
import dev.yidafu.blog.themes.icons.Email
import dev.yidafu.blog.themes.icons.Github
import dev.yidafu.blog.themes.DataModal
import dev.yidafu.blog.themes.simple.pages.SimplePage
import io.github.allangomes.kotlinwind.css.kw
import kotlinx.html.*

abstract class FrontPage(modal: DataModal) : SimplePage(modal) {

  abstract fun MAIN.createContent(): Unit

  override val bodyBlock: BODY.() -> Unit = {
    nav("m-auto") {
      style =
        kw.inline {
          padding.y[4].x[6]
          height[12].width[300]
          flex.row.justify_between
          background.white
        }
      div {
        style =
          kw.inline {
            flex.row.items_center
          }
        a("https://www.yidafu.dev") {
          img(classes = "logo") {
            style = kw.inline { height[12] }
            src = "/public/logo.svg"
          }
        }
        span {
          style =
            kw.inline {
              margin.x[3]
              width[0.5].height[5].background.color["#E7E7E7"]
            }
        }

        div {
          style =
            kw.inline {
              flex.row.items_center
              height[8].width[60]
            }
          a(githubUrl) {
            style =
              kw.inline {
                inline_block.margin.right[6]
                size[8]
              }
            Github()
          }

          a("mailto:me@yidafu.dev") {
            style =
              kw.inline {
                inline_block
                size[8]
                text.color["#333"]
              }
            Email()
          }
        }
      }

      div {
        style =
          kw.inline {
            text.center
            flex.row.items_center
          }
        a(Routes.ARTICLE_LIST) {
          style =
            kw.inline {
              font.size[4]
              text.color["#363636"]
              margin.right[20]
            }
          span { +"ARTICLES" }
        }
        a("/about") {
          style =
            kw.inline {
              font.size[4]
              text.color["#363636"]
            }
          span { +"ABOUT" }
        }
      }
    }

    main("main-content") {
      createContent()
    }

    footerComponent()
  }
}
