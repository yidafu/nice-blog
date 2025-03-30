package dev.yidafu.blog.themes.simple.pages.admin.sync

import dev.yidafu.blog.common.Routes
import dev.yidafu.blog.themes.simple.components.Button
import dev.yidafu.blog.i18n.AdminTxt
import dev.yidafu.blog.themes.*
import dev.yidafu.blog.themes.syncLogPage
import io.github.allangomes.kotlinwind.css.*
import kotlinx.html.*

class AdminSyncLogListPage(modal: DataModal) : AdminSyncPage(modal) {
  override fun DIV.createContent() {
    val cellStyle = kw.inline { padding.x[4].y[6] }
    val vo = modal.syncLogPage
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
              +AdminTxt.id.toText()
            }
            td {
              style = cellStyle
              +AdminTxt.status.toText()
            }
            td {
              style = cellStyle
              +AdminTxt.callback_url.toText()
            }
            td {
              style = cellStyle
              +AdminTxt.created_at.toText()
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
                  +AdminTxt.detail.toText()
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
            +AdminTxt.prev_page.toText()
          }
        }
      }
      if (vo.page < vo.pageCount) {
        Button {
          a {
            href = Routes.SYNC_LOG_URL + "?page=${vo.page + 1}"
            +AdminTxt.next_page.toText()
          }
        }
      }
    }
  }
}

class AdminSyncLogListPageProvider : CacheablePageProvider() {
  override fun getName(): String = PageNames.ADMIN_CONFIG_SYNC_LOG_LIST_PAGE

  override fun createPage(modal: DataModal): Page {
    return AdminSyncLogListPage(modal)
  }
}
