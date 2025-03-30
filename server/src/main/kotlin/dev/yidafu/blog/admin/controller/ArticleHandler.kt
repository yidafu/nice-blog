package dev.yidafu.blog.admin.handler

import dev.yidafu.blog.admin.views.pages.article.AdminArticleListPage
import dev.yidafu.blog.common.converter.ArticleConvertor
import dev.yidafu.blog.common.ext.html
import dev.yidafu.blog.common.ext.render
import dev.yidafu.blog.common.query.PageQuery
import dev.yidafu.blog.common.services.ArticleService
import dev.yidafu.blog.common.vo.AdminArticlePageVO
import dev.yidafu.blog.themes.PageNames
import io.vertx.ext.web.RoutingContext
import org.koin.core.annotation.Single
import org.mapstruct.factory.Mappers
import org.slf4j.LoggerFactory

@Single
class ArticleHandler(
  private val articleService: ArticleService,
) {
  private val log = LoggerFactory.getLogger(ArticleHandler::class.java)
  private val convertor = Mappers.getMapper(ArticleConvertor::class.java)

  fun articleListPage(ctx: RoutingContext) {
    val pageNum = ctx.queryParam("page").ifEmpty { listOf("1") }[0].toInt()
    val pageSize = ctx.queryParam("size").ifEmpty { listOf("10") }[0].toInt()

    val query = PageQuery(pageNum, pageSize)
    val (total, list) = articleService.getListByPage(query)
    val vo =
      AdminArticlePageVO(
        pageNum,
        pageSize,
        total,
        list,
      )
    ctx.html(AdminArticleListPage::class.java, vo)
  }

  suspend fun articleDetailPage(ctx: RoutingContext) {
    val id = ctx.pathParam("id")[0].digitToInt()
    val model = articleService.getById(id)
    if (model == null) {
      ctx.redirect("/404")
      return
    }
    val vo = convertor.toVO(model)
    ctx.render(PageNames.ARTICLE_DETAIL, vo)
  }

  suspend fun articleHistoryListPage(ctx: RoutingContext) {
    val id = ctx.pathParam("id")[0].digitToInt()
    val model = articleService.getById(id)
    if (model == null) {
      ctx.redirect("/404")
      return
    }
    val vo = convertor.toVO(model)
    ctx.render(PageNames.ARTICLE_DETAIL, vo)
  }

  suspend fun articleStatisticPage(ctx: RoutingContext) {
    val id = ctx.pathParam("id")[0].digitToInt()
    val model = articleService.getById(id)
    if (model == null) {
      ctx.redirect("/404")
      return
    }
    val vo = convertor.toVO(model)
    ctx.render(PageNames.ARTICLE_DETAIL, vo)
  }
}
