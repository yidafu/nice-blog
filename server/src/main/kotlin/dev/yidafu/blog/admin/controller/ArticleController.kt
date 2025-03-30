package dev.yidafu.blog.admin.controller

import dev.yidafu.blog.common.Routes
import dev.yidafu.blog.common.converter.ArticleConvertor
import dev.yidafu.blog.common.ext.render
import dev.yidafu.blog.common.query.PageQuery
import dev.yidafu.blog.common.services.ArticleService
import dev.yidafu.blog.common.vo.PaginationVO
import dev.yidafu.blog.ksp.annotation.Controller
import dev.yidafu.blog.ksp.annotation.Get
import dev.yidafu.blog.themes.PageNames
import io.vertx.ext.web.RoutingContext
import org.koin.core.annotation.Single
import org.mapstruct.factory.Mappers
import org.slf4j.LoggerFactory

@Controller
@Single
class ArticleController(
  private val articleService: ArticleService,
) {
  private val log = LoggerFactory.getLogger(ArticleController::class.java)
  private val convertor = Mappers.getMapper(ArticleConvertor::class.java)

  @Get(Routes.ADMIN_ARTICLE_LIST)
  fun articleListPage(ctx: RoutingContext) {
    val pageNum = ctx.queryParam("page").ifEmpty { listOf("1") }[0].toInt()
    val pageSize = ctx.queryParam("size").ifEmpty { listOf("10") }[0].toInt()

    val query = PageQuery(pageNum, pageSize)
    val (total, list) = articleService.getListByPage(query)
    val voList = convertor.toVO(list)
    val vo =
      PaginationVO(
        pageNum,
        pageSize,
        total,
        voList,
      )
    ctx.render(PageNames.ADMIN_ARTICLE_LIST, vo)
  }

  @Get(Routes.ADMIN_ARTICLE_DETAIL)
  @Get(Routes.ADMIN_ARTICLE_DETAIL_2)
  suspend fun articleDetailPage(ctx: RoutingContext) {
    val id = ctx.pathParam("id")[0].digitToInt()
    val model = articleService.getById(id)
    if (model == null) {
      ctx.redirect("/404")
      return
    }
    val vo = convertor.toVO(model)
    ctx.render(PageNames.ADMIN_ARTICLE_DETAIL, vo)
  }

  @Get(Routes.ADMIN_ARTICLE_HISTORY)
  suspend fun articleHistoryListPage(ctx: RoutingContext) {
    val id = ctx.pathParam("id")[0].digitToInt()
    val model = articleService.getById(id)
    if (model == null) {
      ctx.redirect("/404")
      return
    }
    val vo = convertor.toVO(model)
    ctx.render(PageNames.ADMIN_ARTICLE_DETAIL, vo)
  }

  @Get(Routes.ADMIN_ARTICLE_STATISTIC)
  suspend fun articleStatisticPage(ctx: RoutingContext) {
    val id = ctx.pathParam("id")[0].digitToInt()
    val model = articleService.getById(id)
    if (model == null) {
      ctx.redirect("/404")
      return
    }
    val vo = convertor.toVO(model)
    ctx.render(PageNames.ADMIN_ARTICLE_DETAIL, vo)
  }
}
