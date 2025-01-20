package dev.yidafu.blog.fe.views.layouts

import dev.yidafu.blog.common.Routes
import dev.yidafu.blog.common.view.components.footerBlock
import dev.yidafu.blog.common.view.icons.Email
import dev.yidafu.blog.common.view.icons.Github
import dev.yidafu.blog.common.view.layouts.BaseLayout
import dev.yidafu.blog.common.vo.PageVO
import io.github.allangomes.kotlinwind.css.kw
import kotlinx.css.Color
import kotlinx.css.hex
import kotlinx.css.px
import kotlinx.html.*

val HEADER_WIDTH = 1580
val CONTENT_WIDTH = 1288.px
val COVER_HEIGHT = 720.px
val COVER_WIDTH = CONTENT_WIDTH - 8.px
val PARAGRAPH_WIDTH = 780.px
val IMG_WIDTH = 1200.px
val ARTICLE_WIDTH = 1200.px

val TEXT_COLOR_PRIMARY = Color("#4A4A4A")
val TEXT_COLOR_SECONDARY = Color("#9B9B9B")

val COLOR_PRIMARY = Color("#1E1E1E")
val BACKGROUND_COLOR = Color("#FAFAFA")
val HEADER_COLOR = COLOR_PRIMARY

val CODE_BACKGROUND_COLOR = hex(0xfafafa)

class FrontendLayout(val vo: PageVO) : BaseLayout(
  headScript = {
    link {
      rel = "stylesheet"
      href = "/public/github-markdown-light.css"
    }
  },
) {
  override val backgroundColor = "#fff"

  override fun layout(block: DIV.() -> Unit): TagConsumer<String> {
    return super.layout {
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
            a(vo.githubUrl) {
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
        div {
          block()
        }
      }

      footerBlock()
    }
  }
}
