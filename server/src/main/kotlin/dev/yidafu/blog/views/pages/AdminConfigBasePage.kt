package dev.yidafu.blog.views.pages

import dev.yidafu.blog.bean.vo.AdminBaseVo
import dev.yidafu.blog.common.Routes
import dev.yidafu.blog.i18n.AdminTxt
import dev.yidafu.blog.views.components.TabOption
import dev.yidafu.blog.views.components.tabs
import dev.yidafu.blog.views.icons.AppearanceIcon
import dev.yidafu.blog.views.icons.EnglishIcon
import dev.yidafu.blog.views.icons.SourceIcon
import dev.yidafu.blog.views.icons.SyncIcon
import dev.yidafu.blog.views.layouts.AdminLayout
import kotlinx.html.TagConsumer

abstract class AdminConfigBasePage(protected val vo: AdminBaseVo) {
  private fun getOptions(vo: AdminBaseVo) = listOf(
    TabOption(AdminTxt.appearance.toString(vo.locale), Routes.CONFIG_APPEARANCE_URL, AppearanceIcon(), Routes.CONFIG_APPEARANCE_URL == vo.currentPath),
    TabOption(AdminTxt.sync.toString(vo.locale), Routes.CONFIG_SYNC_URL, SyncIcon(), Routes.CONFIG_SYNC_URL == vo.currentPath),
    TabOption(AdminTxt.data_source.toString(vo.locale), Routes.CONFIG_DATA_SOURCE_URL, SourceIcon(), Routes.CONFIG_DATA_SOURCE_URL == vo.currentPath),
  )

  private val layout = AdminLayout(vo)
  protected val tagConsumer: TagConsumer<String>
    get() = layout.tagConsumer

  abstract fun getContent()

  fun render(): TagConsumer<String> {
    return layout.layout {
      tabs(options = getOptions(vo)) {
        getContent()
      }
    }
  }
}
