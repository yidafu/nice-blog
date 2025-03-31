package dev.yidafu.blog.themes.simple.pages.admin.config

import dev.yidafu.blog.common.Routes
import dev.yidafu.blog.i18n.AdminTxt
import dev.yidafu.blog.themes.DataModal
import dev.yidafu.blog.themes.articleDetail
import dev.yidafu.blog.themes.icons.AppearanceIcon
import dev.yidafu.blog.themes.icons.SourceIcon
import dev.yidafu.blog.themes.icons.SyncIcon
import dev.yidafu.blog.themes.simple.components.TabOption
import dev.yidafu.blog.themes.simple.components.tabs
import dev.yidafu.blog.themes.simple.pages.admin.AdminPage
import kotlinx.html.DIV

abstract class AdminConfigPage(modal: DataModal) : AdminPage(modal) {
  private fun getRoute(rawPath: String): String {
    return rawPath.replace(":id", modal.articleDetail.id.toString())
  }

  private fun getOptions() =
    listOf(
      TabOption(
        AdminTxt.appearance.toText(),
        Routes.CONFIG_APPEARANCE_URL,
        AppearanceIcon(),
        Routes.CONFIG_APPEARANCE_URL == currentPath,
      ),
      TabOption(AdminTxt.sync.toText(), Routes.CONFIG_SYNC_URL, SyncIcon(), Routes.CONFIG_SYNC_URL == currentPath),
      TabOption(
        AdminTxt.data_source.toText(),
        Routes.CONFIG_DATA_SOURCE_URL,
        SourceIcon(),
        Routes.CONFIG_DATA_SOURCE_URL == currentPath,
      ),
    )

  abstract fun DIV.createContent(): Unit

  override fun DIV.layoutBlock() {
    tabs(options = getOptions()) {
      createContent()
    }
  }
}
