package dev.yidafu.blog.admin.views.pages.sync

import dev.yidafu.blog.common.vo.AdminSyncTaskVO
import io.github.allangomes.kotlinwind.css.I50
import io.github.allangomes.kotlinwind.css.kw
import kotlinx.html.DIV
import kotlinx.html.div
import kotlinx.html.pre
import kotlinx.html.style

class AdminSyncLogPage(override val vo: AdminSyncTaskVO) : AdminSyncBasePage<AdminSyncTaskVO>() {
  override fun getContent(): DIV.() -> Unit =
    {
      div {
        style =
          kw.inline {
            padding[2]
            border.rounded[8]
            background.gray[I50]
          }
        pre {
          style = "text-wrap: auto;"
          +vo.syncTask.logs
        }
      }
    }
}
