package dev.yidafu.blog.admin.views.pages.sync

import dev.yidafu.blog.admin.views.layouts.AdminLayout
import dev.yidafu.blog.common.Routes
import dev.yidafu.blog.common.view.icons.SyncIcon
import dev.yidafu.blog.common.view.icons.SyncLogIcon
import dev.yidafu.blog.common.view.tpl.PageTemplate
import dev.yidafu.blog.common.views.components.TabOption
import dev.yidafu.blog.common.views.components.tabs
import dev.yidafu.blog.common.vo.PageVO
import dev.yidafu.blog.i18n.AdminTxt
import kotlinx.html.DIV

abstract class AdminSyncBasePage<T : PageVO>() : PageTemplate<T>() {
  private fun getOptions(vo: PageVO) =
    listOf(
      TabOption(AdminTxt.sync_operate.toString(vo.locale), Routes.SYNC_OPERATE_URL, SyncIcon(), Routes.SYNC_OPERATE_URL == vo.currentPath),
      TabOption(AdminTxt.sync_log.toString(vo.locale), Routes.SYNC_LOG_URL, SyncLogIcon(), Routes.SYNC_LOG_URL == vo.currentPath),
    )

//  protected val tagConsumer: TagConsumer<String>
//    get() = layout.tagConsumer

  abstract fun getContent(): DIV.() -> Unit

  override fun render(): String {
    val layout = AdminLayout(vo)
    return layout.layout {
      tabs(options = getOptions(vo)) {
        val context = getContent()
        context()
      }
    }.finalize()
  }
}
