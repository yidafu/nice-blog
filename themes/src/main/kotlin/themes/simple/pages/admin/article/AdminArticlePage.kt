package dev.yidafu.blog.themes.simple.pages.admin.article

import dev.yidafu.blog.common.Routes
import dev.yidafu.blog.themes.icons.Article
import dev.yidafu.blog.themes.icons.ArticleHistory
import dev.yidafu.blog.themes.icons.ArticleStatistic
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
        AdminTxt.article.toText(),
        getRoute(Routes.ADMIN_ARTICLE_DETAIL),
        Article(),
        Routes.ARTICLE_DETAIL == currentPath,
      ),
      TabOption(
        AdminTxt.article_statistic.toText(),
        getRoute(Routes.ADMIN_ARTICLE_STATISTIC),
        ArticleStatistic(),
        Routes.ADMIN_ARTICLE_STATISTIC == currentPath,
      ),
      TabOption(
        AdminTxt.article_history.toText(),
        getRoute(Routes.ADMIN_ARTICLE_HISTORY),
        ArticleHistory(),
        Routes.ADMIN_ARTICLE_HISTORY == currentPath,
      ),
    )

  abstract fun DIV.createContent(): Unit

  override fun DIV.layoutBlock() {
    tabs(options = getOptions()) {
      createContent()
    }
  }
}
