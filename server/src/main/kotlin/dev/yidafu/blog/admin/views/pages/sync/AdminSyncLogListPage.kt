package dev.yidafu.blog.admin.views.pages.sync

import dev.yidafu.blog.common.Routes
import dev.yidafu.blog.common.view.components.Button
import dev.yidafu.blog.common.vo.AdminSyncTaskListVO
import dev.yidafu.blog.i18n.AdminTxt
import io.github.allangomes.kotlinwind.css.*
import kotlinx.html.*

class AdminSyncLogListPage(override val vo: AdminSyncTaskListVO) : AdminSyncBasePage<AdminSyncTaskListVO>() {
  override fun getContent(): DIV.() -> Unit =
    {
      val cellStyle = kw.inline { padding.x[4].y[6] }
      div {
        style = kw.inline { background.gray[I50] }
        table {
          attributes["border-collapse"] = "collapse"
          style =
            kw.inline {
              text.gray[I500].left
              width["100%"]
            }
          thead {
            style =
              kw.inline {
                background.gray[I50]
                text.gray[I700]
                font.xs
              }
            tr {
              td {
                style = cellStyle
                +AdminTxt.id.toString(vo.locale)
              }
              td {
                style = cellStyle
                +AdminTxt.status.toString(vo.locale)
              }
              td {
                style = cellStyle
                +AdminTxt.callback_url.toString(vo.locale)
              }
              td {
                style = cellStyle
                +AdminTxt.created_at.toString(vo.locale)
              }
            }
          }
          tbody {
            vo.list.forEach { i ->
              tr {
                style =
                  kw.inline {
                    background.white
                    border.bottom[1].gray[I900]
                  }
                td {
                  style = cellStyle
                  +i.id.toString()
                }
                td {
                  style = cellStyle
                  +i.status.toString()
                }
                td {
                  style = cellStyle
                  +i.callbackUrl
                }
                td {
                  style = cellStyle
                  +i.createdAt.toString()
                }
                td {
                  style = cellStyle
                  a {
                    href = Routes.SYNC_LOG_DETAIL_URL + "?uuid=" + i.uuid
                    +AdminTxt.detail.toString(vo.locale)
                  }
                }
              }
            }
          }
        }
      }

      // pagination
      div {
        style =
          kw.inline {
            padding[3]
            flex.row.justify_between
          }
        if (vo.page > 1) {
          Button {
            a {
              href = Routes.SYNC_LOG_URL + "?page=${vo.page - 1}"
              +AdminTxt.prev_page.toString(vo.locale)
            }
          }
        }
        if (vo.page < vo.pageCount) {
          Button {
            a {
              href = Routes.SYNC_LOG_URL + "?page=${vo.page + 1}"
              +AdminTxt.next_page.toString(vo.locale)
            }
          }
        }
      }
    }
}
