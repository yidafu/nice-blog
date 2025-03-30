package dev.yidafu.blog.themes.simple.pages.admin

import dev.yidafu.blog.common.Routes
import dev.yidafu.blog.themes.simple.components.footerComponent
import dev.yidafu.blog.common.view.icons.*
import dev.yidafu.blog.i18n.AdminTxt
import dev.yidafu.blog.themes.DataModal
import dev.yidafu.blog.themes.icons.*
import dev.yidafu.blog.themes.simple.pages.SimplePage
import io.github.allangomes.kotlinwind.css.*
import kotlinx.html.*


fun FlowOrInteractiveOrPhrasingContent.linkItem(
  link: String,
  url: String,
  active: Boolean = false,
) {
  a {
    style =
      kw.inline {
        margin.top[4]
        font.size[6]
        margin.right[4]
        if (active) {
          text.green[I800]
          border.bottom[1].green[I600]
        } else {
          text.black
        }
      }
    href = url
    +link
  }
}

abstract class AdminPage(modal: DataModal) : SimplePage(modal) {
  internal fun TR.cell(text: String) {
    td {
      style = kw.inline { padding.x[4].y[6] }
      +text
    }
  }

  internal fun TR.cell(block: TD.() -> Unit) {
    td {
      style = kw.inline { padding.x[4].y[6] }
      block()
    }
  }

  private val linkList =
    listOf(
//    Triple(AdminTxt.appearance.toText(), Routes.CONFIG_APPEARANCE_URL, vo.currentPath == Routes.CONFIG_APPEARANCE_URL),
      Triple(
        AdminTxt.article.toText(),
        Routes.ADMIN_ARTICLE_LIST,
        currentPath.startsWith(Routes.ADMIN_ARTICLE_LIST),
      ),
      Triple(
        AdminTxt.configuration.toText(),
        Routes.CONFIGURATION_URL,
        currentPath.startsWith(Routes.CONFIGURATION_URL),
      ),
      Triple(AdminTxt.sync.toText(), Routes.SYNC_URL, currentPath == Routes.SYNC_URL),
      Triple(AdminTxt.pictures.toText(), Routes.PICTURES_URL, currentPath == Routes.PICTURES_URL),
    )
  override val headBlock: HEAD.() -> Unit = {
    script {
      src = "/public/htmx.min.js"
    }
    script {
      src = "/public/htmx-ext-sse.js"
    }
  }

  override val bodyBlock: BODY.() -> Unit = {
    div {
      style =
        kw.inline {
          border.gray[I200].bottom[1]
          background.white
        }

      nav("m-auto") {
        style =
          kw.inline {
            padding[6]
            flex.row.items_center.justify_between.wrap
            max_width[256]
          }
        div {
          style =
            kw.inline {
              flex.row.items_center.shrink_0
              text.black
              margin.right[6]
            }

          span {
            style = kw.inline { font.size[XL5].weight_600 }
            +siteTitle
          }
        }

        div {
          style = kw.inline { flex.row.fill.items_center.justify_between }
          div {
            style = kw.inline { font.size[SM] }
            linkList.forEach { item ->
              linkItem(item.first, item.second, item.third)
            }
          }

          div {
            style = kw.inline { flex.row }

            div("dropdown") {
              style = kw.inline { relative }
              style =
                kw.inline {
                  font.size[SM]
                  width[8].height[6].margin.right[3]
                }
              Language()
              div("dropdown-content shadow") {
                style =
                  kw.inline {
                    absolute.min_width[40].background.white
                    hidden
                  }
                a {
                  style =
                    kw.inline {
                      flex.row.items_center
                      text.gray[I600]
                      padding.x[4].y[3]
                    }
                  href = "?lang=en-US"
                  div {
                    style = kw.inline { inline_block.width[4].height[4].margin.right[2] }
                    English()
                  }
                  +"English"
                }
                a {
                  style =
                    kw.inline {
                      flex.row.items_center
                      text.gray[I600]
                      padding.x[4].y[3]
                    }
                  href = "?lang=zh-CN"
                  div {
                    style = kw.inline { inline_block.width[4].height[4].margin.right[2] }
                    Chinese()
                  }
                  +"中文"
                }
              }
            }

            a {
              style =
                kw.inline {
                  font.size[SM]
                  width[6].height[6]
                }
              href = githubUrl
              Github()
            }

            a {
              style =
                kw.inline {
                  font.size[SM]
                  height[6]
                  text.slate[I600]
                  margin.left[4]
                  flex.row.items_center
                }
              href = Routes.LOGOUT_URL
              span {
                style =
                  kw.inline {
                    font.size[SM]
                  }
                +AdminTxt.logout.toText()
              }
              i {
                style = kw.inline { size[5] }
                Logout()
              }
            }
          }
        }
      }
    }

    div("m-auto") {
      style = kw.inline {
          max_width[256]
          padding[6]
          background.white
          border.rounded[LG]
          margin.y[8]
        }
      layoutBlock()
    }

    footerComponent()
  }

  abstract fun DIV.layoutBlock()
}
