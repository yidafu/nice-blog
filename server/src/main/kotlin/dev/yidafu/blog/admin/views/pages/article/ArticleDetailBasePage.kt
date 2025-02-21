package dev.yidafu.blog.admin.views.pages.article

import dev.yidafu.blog.admin.views.layouts.AdminLayout
import dev.yidafu.blog.common.Routes
import dev.yidafu.blog.common.view.icons.Article
import dev.yidafu.blog.common.view.icons.ArticleHistory
import dev.yidafu.blog.common.view.icons.ArticleStatistic
import dev.yidafu.blog.common.view.tpl.PageTemplate
import dev.yidafu.blog.common.views.components.TabOption
import dev.yidafu.blog.common.views.components.tabs
import dev.yidafu.blog.common.vo.ArticleVO
import dev.yidafu.blog.i18n.AdminTxt
import kotlinx.html.DIV

abstract class ArticleDetailBasePage(
  override val vo: ArticleVO,
) : PageTemplate<ArticleVO>() {
  private fun getRoute(rawPath: String): String {
    return rawPath.replace(":id", vo.id.toString())
  }

  private fun getOptions(vo: ArticleVO) =
    listOf(
      TabOption(
        AdminTxt.article_detail.toString(vo.locale),
        getRoute(Routes.ARTICLE_ADMIN_DETAIL),
        Article(),
        Routes.ARTICLE_DETAIL == vo.currentPath,
      ),
      TabOption(
        AdminTxt.article_statistic.toString(vo.locale),
        getRoute(Routes.ARTICLE_ADMIN_STATISTIC),
        ArticleStatistic(),
        Routes.ARTICLE_ADMIN_STATISTIC == vo.currentPath,
      ),
      TabOption(
        AdminTxt.article_history.toString(vo.locale),
        getRoute(Routes.ARTICLE_ADMIN_HISTORY),
        ArticleHistory(),
        Routes.ARTICLE_ADMIN_HISTORY == vo.currentPath,
      ),
    )

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
