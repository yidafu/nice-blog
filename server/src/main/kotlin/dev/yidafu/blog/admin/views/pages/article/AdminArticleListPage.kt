package dev.yidafu.blog.admin.views.pages.article

import dev.yidafu.blog.admin.views.layouts.AdminLayout
import dev.yidafu.blog.common.Routes
import dev.yidafu.blog.common.view.components.Button
import dev.yidafu.blog.common.view.tpl.PageTemplate
import dev.yidafu.blog.common.vo.AdminArticleListVO
import dev.yidafu.blog.i18n.AdminTxt
import io.github.allangomes.kotlinwind.css.*
import kotlinx.html.*
import org.intellij.markdown.html.urlEncode

class AdminArticleListPage(
  override val vo: AdminArticleListVO,
) : PageTemplate<AdminArticleListVO>() {
  private fun TR.cell(text: String) {
    td {
      style = kw.inline { padding.x[4].y[6] }
      +text
    }
  }

  private fun TR.cell(block: TD.() -> Unit) {
    td {
      style = kw.inline { padding.x[4].y[6] }
      block()
    }
  }

  override fun render(): String {
    val layout = AdminLayout(vo)
    return layout.layout {
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
              cell(AdminTxt.id.toString(vo.locale))
              cell(AdminTxt.column_title.toString(vo.locale))
              cell(AdminTxt.status.toString(vo.locale))
              cell(AdminTxt.column_summary.toString(vo.locale))
              cell(AdminTxt.column_updated_at.toString(vo.locale))
              cell(AdminTxt.column_detail.toString(vo.locale))
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
                    href = Routes.ARTICLE_DETAIL.replace(":identifier", urlEncode((i.id ?: "").toString()))
                    +AdminTxt.column_detail.toString(vo.locale)
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
    }.finalize()
  }
}
