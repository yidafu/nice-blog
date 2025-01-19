package dev.yidafu.blog.admin.views.layouts

import dev.yidafu.blog.common.Routes
import dev.yidafu.blog.common.view.components.footerBlock
import dev.yidafu.blog.common.view.icons.*
import dev.yidafu.blog.common.view.layouts.BaseLayout
import dev.yidafu.blog.common.vo.PageVO
import dev.yidafu.blog.i18n.AdminTxt
import io.github.allangomes.kotlinwind.css.*
import kotlinx.html.*

fun FlowOrInteractiveOrPhrasingContent.link(
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

class AdminLayout<P : PageVO>(private val vo: P) : BaseLayout(headScript = {
  script {
    src = "/public/htmx.min.js"
  }
  script {
    src = "/public/htmx-ext-sse.js"
  }
}) {
  private val linkList =
    listOf(
//    Triple(AdminTxt.appearance.toString(vo.locale), Routes.CONFIG_APPEARANCE_URL, vo.currentPath == Routes.CONFIG_APPEARANCE_URL),
      Triple(
        AdminTxt.configuration.toString(vo.locale),
        Routes.CONFIGURATION_URL,
        vo.currentPath.startsWith(Routes.CONFIGURATION_URL),
      ),
      Triple(AdminTxt.sync.toString(vo.locale), Routes.SYNC_URL, vo.currentPath == Routes.SYNC_URL),
      Triple(AdminTxt.pictures.toString(vo.locale), Routes.PICTURES_URL, vo.currentPath == Routes.PICTURES_URL),
    )

  override fun layout(laoutBlock: DIV.() -> Unit): TagConsumer<String> {
    return super.layout {
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
              +vo.siteTitle
            }
          }

          div {
            style = kw.inline { flex.row.fill.items_center.justify_between }
            div {
              style = kw.inline { font.size[SM] }

              linkList.forEach { item ->

                link(item.first, item.second, item.third)
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
                href = vo.githubUrl
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
                  +AdminTxt.logout.toString(vo.locale)
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
        style =
          kw.inline {
            max_width[256]
            padding[6]
            background.white
            border.rounded[LG]
            margin.y[8]
          }
        laoutBlock()
      }

      footerBlock()
    }
  }
}
