package dev.yidafu.blog.themes.simple.pages.admin.article

import dev.yidafu.blog.common.Routes
import dev.yidafu.blog.common.vo.ArticleVO
import dev.yidafu.blog.common.vo.PaginationVO
import dev.yidafu.blog.i18n.AdminTxt
import dev.yidafu.blog.themes.*
import dev.yidafu.blog.themes.simple.components.Button
import dev.yidafu.blog.themes.simple.pages.admin.AdminPage
import kotlinx.html.*

class AdminArticleListPage(modal: DataModal) : AdminPage(modal) {
  override fun DIV.layoutBlock() {
    val vo = modal.obj<PaginationVO<ArticleVO>>("pagination") ?: PaginationVO()
    div {
      classes = setOf("bg-gray-50")
      table {
        classes = setOf("admin-table")
        thead {
          classes = setOf("admin-table__head")
          tr {
            cell(AdminTxt.id.toText())
            cell(AdminTxt.column_title.toText())
            cell(AdminTxt.status.toText())
            cell(AdminTxt.column_summary.toText())
            cell(AdminTxt.column_updated_at.toText())
            cell(AdminTxt.column_detail.toText())
          }
        }
        tbody {
          vo.list.forEach { i ->
            tr {
              classes = setOf("admin-table__row")
              cell(i.id.toString())
              cell(i.title)
              cell(i.status.toString())
              cell(i.summary ?: "-")
              cell(i.updatedAt.toString())
              cell {
                a {
                  href = Routes.ADMIN_ARTICLE_DETAIL.replace(":id", i.id.toString())
                  +AdminTxt.column_detail.toText()
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

class AdminArticleListPageProvider : CacheablePageProvider() {
  override fun getName(): String = PageNames.ADMIN_ARTICLE_LIST

  override fun createPage(modal: DataModal): Page = AdminArticleListPage(modal)
}
