package dev.yidafu.blog.themes.simple.pages.admin.article

import dev.yidafu.blog.common.Routes
import dev.yidafu.blog.common.view.components.Button
import dev.yidafu.blog.i18n.AdminTxt
import dev.yidafu.blog.themes.*
import io.github.allangomes.kotlinwind.css.*
import kotlinx.html.*
import java.net.URLEncoder

class AdminArticleListPage(modal: DataModal) : AdminArticlePage(modal){
  override fun DIV.createContent() {
    val vo = modal.articlePage
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
              style =
                kw.inline {
                  background.white
                  border.bottom[1].gray[I900]
                }
              cell(i.id.toString())
              cell(i.title)
              cell(i.status.toString())
              cell(i.summary ?: "-")
              cell(i.updatedAt.toString())
              cell {
                a {
                  href = Routes.ARTICLE_DETAIL.replace(":identifier", URLEncoder.encode((i.id.toString())))
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


class AdminArticleListPageProvider : CacheablePageProvider() {
  override fun getName(): String = PageNames.ADMIN_ARTICLE_LIST

  override fun createPage(modal: DataModal): Page = AdminArticleListPage(modal)
}
