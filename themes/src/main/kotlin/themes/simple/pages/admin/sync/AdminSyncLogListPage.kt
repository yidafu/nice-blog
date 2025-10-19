package dev.yidafu.blog.themes.simple.pages.admin.sync

import dev.yidafu.blog.common.Routes
import dev.yidafu.blog.common.vo.AdminSyncTaskVO
import dev.yidafu.blog.common.vo.PaginationVO
import dev.yidafu.blog.i18n.AdminTxt
import dev.yidafu.blog.themes.*
import dev.yidafu.blog.themes.simple.components.Button
import kotlinx.html.*

class AdminSyncLogListPage(modal: DataModal) : AdminSyncPage(modal) {
  override fun DIV.createContent() {
    val vo = modal.obj<PaginationVO<AdminSyncTaskVO>>("pagination") ?: PaginationVO()
    div {
      classes = setOf("bg-gray-50")
      table {
        classes = setOf("admin-table")
        thead {
          classes = setOf("admin-table__head")
          tr {
            td {
              classes = setOf("admin-table__cell")
              +AdminTxt.id.toText()
            }
            td {
              classes = setOf("admin-table__cell")
              +AdminTxt.status.toText()
            }
            td {
              classes = setOf("admin-table__cell")
              +AdminTxt.callback_url.toText()
            }
            td {
              classes = setOf("admin-table__cell")
              +AdminTxt.created_at.toText()
            }
          }
        }
        tbody {
          vo.list.forEach { i ->
            tr {
              classes = setOf("admin-table__row")
              td {
                classes = setOf("admin-table__cell")
                +i.id.toString()
              }
              td {
                classes = setOf("admin-table__cell")
                +i.status.toString()
              }
              td {
                classes = setOf("admin-table__cell")
                +i.callbackUrl
              }
              td {
                classes = setOf("admin-table__cell")
                +i.createdAt.toString()
              }
              td {
                classes = setOf("admin-table__cell")
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
      classes = setOf("p-3", "flex-row", "justify-between")
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
