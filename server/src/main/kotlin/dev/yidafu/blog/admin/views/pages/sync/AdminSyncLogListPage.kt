package dev.yidafu.blog.admin.views.pages.sync

import dev.yidafu.blog.common.vo.AdminSyncTaskListVO
import dev.yidafu.blog.common.vo.AdminSynchronousVO
import dev.yidafu.blog.common.vo.PageVO
import dev.yidafu.blog.i18n.AdminTxt
import io.github.allangomes.kotlinwind.css.*
import kotlinx.html.*

class AdminSyncLogPage(override val vo: AdminSyncTaskListVO) : AdminSyncBasePage<AdminSyncTaskListVO>() {

  override fun getContent(): DIV.() -> Unit = {
    val cellStyle = kw.inline { padding.x[4].y[6] }
    div {
      style = kw.inline { background.gray[I50] }
      table {
        attributes["border-collapse"] = "collapse"
        style = kw.inline { text.gray[I500].left; width["100%"] }
        thead {
          style = kw.inline { background.gray[I50]; text.gray[I700]; font.xs }
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
              style = kw.inline { background.white; border.bottom[1].gray[I900] }
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
            }
          }
        }
      }
    }
  }
}
