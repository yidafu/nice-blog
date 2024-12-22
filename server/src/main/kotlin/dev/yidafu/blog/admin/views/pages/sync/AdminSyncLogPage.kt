package dev.yidafu.blog.admin.views.pages.sync

import dev.yidafu.blog.common.vo.AdminSynchronousVO
import dev.yidafu.blog.common.vo.PageVO
import dev.yidafu.blog.i18n.AdminTxt
import io.github.allangomes.kotlinwind.css.I700
import io.github.allangomes.kotlinwind.css.LG
import io.github.allangomes.kotlinwind.css.kw
import kotlinx.html.DIV
import kotlinx.html.button
import kotlinx.html.div
import kotlinx.html.style

class AdminSyncLogPage(override val vo: PageVO) : AdminSyncBasePage<AdminSynchronousVO>() {

  override fun getContent(): DIV.() -> Unit = {
    div {
      button {
        style = kw.inline {
          padding.x[4].y[3]; border.rounded[LG];
          text.white;
          background.blue[I700];
        }
        +AdminTxt.sync_start.toString(vo.locale)
      }
    }
  }
}
