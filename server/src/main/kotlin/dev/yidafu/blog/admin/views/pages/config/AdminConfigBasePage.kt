package dev.yidafu.blog.admin.views.pages.config

import dev.yidafu.blog.common.Routes
import dev.yidafu.blog.i18n.AdminTxt
import dev.yidafu.blog.common.views.components.TabOption
import dev.yidafu.blog.common.views.components.tabs
import dev.yidafu.blog.common.view.icons.AppearanceIcon
import dev.yidafu.blog.common.view.icons.SourceIcon
import dev.yidafu.blog.common.view.icons.SyncIcon
import dev.yidafu.blog.admin.views.layouts.AdminLayout
import dev.yidafu.blog.common.view.tpl.Page
import dev.yidafu.blog.common.vo.PageVO
import kotlinx.html.DIV

abstract class AdminConfigBasePage<T: PageVO>(override val vo: T) : Page<T>() {
  private fun getOptions(vo: PageVO) = listOf(
    TabOption(AdminTxt.appearance.toString(vo.locale), Routes.CONFIG_APPEARANCE_URL, AppearanceIcon(), Routes.CONFIG_APPEARANCE_URL == vo.currentPath),
    TabOption(AdminTxt.sync.toString(vo.locale), Routes.CONFIG_SYNC_URL, SyncIcon(), Routes.CONFIG_SYNC_URL == vo.currentPath),
    TabOption(AdminTxt.data_source.toString(vo.locale), Routes.CONFIG_DATA_SOURCE_URL, SourceIcon(), Routes.CONFIG_DATA_SOURCE_URL == vo.currentPath),
  )


  abstract fun getContent(vo: T): DIV.() -> Unit

  override fun render(): String {
     val layout = AdminLayout(vo)

    return layout.layout {
      tabs(options = getOptions(vo)) {
        val content = getContent(vo)
        content()
      }
    }.finalize()
  }
}
