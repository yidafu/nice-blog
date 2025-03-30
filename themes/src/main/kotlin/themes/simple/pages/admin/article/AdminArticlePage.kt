package dev.yidafu.blog.themes.simple.pages.admin.article

import dev.yidafu.blog.common.Routes
import dev.yidafu.blog.common.view.icons.Article
import dev.yidafu.blog.common.view.icons.ArticleHistory
import dev.yidafu.blog.common.view.icons.ArticleStatistic
import dev.yidafu.blog.i18n.AdminTxt
import dev.yidafu.blog.themes.DataModal
import dev.yidafu.blog.themes.articleDetail
import dev.yidafu.blog.themes.simple.components.TabOption
import dev.yidafu.blog.themes.simple.components.tabs
import dev.yidafu.blog.themes.simple.pages.admin.AdminPage
import kotlinx.html.DIV

abstract class AdminArticlePage(modal: DataModal) : AdminPage(modal) {
  private fun getRoute(rawPath: String): String {
    return rawPath.replace(":id", modal.articleDetail.id.toString())
  }

  private fun getOptions() =
    listOf(
      TabOption(
        AdminTxt.article_detail.toText(),
        getRoute(Routes.ARTICLE_ADMIN_DETAIL),
        Article(),
        Routes.ARTICLE_DETAIL == currentPath,
      ),
      TabOption(
        AdminTxt.article_statistic.toText(),
        getRoute(Routes.ARTICLE_ADMIN_STATISTIC),
        ArticleStatistic(),
        Routes.ARTICLE_ADMIN_STATISTIC == currentPath,
      ),
      TabOption(
        AdminTxt.article_history.toText(),
        getRoute(Routes.ARTICLE_ADMIN_HISTORY),
        ArticleHistory(),
        Routes.ARTICLE_ADMIN_HISTORY == currentPath,
      ),
    )

  abstract fun DIV.createContent(): Unit

  override fun DIV.layoutBlock() {
    tabs(options = getOptions())
    createContent()
  }
}


