package dev.yidafu.blog.admin.controller

import dev.yidafu.blog.common.Routes
import dev.yidafu.blog.common.converter.ArticleConvertor
import dev.yidafu.blog.common.ext.render
import dev.yidafu.blog.common.query.PageQuery
import dev.yidafu.blog.common.services.ArticleService
import dev.yidafu.blog.common.vo.PaginationVO
import dev.yidafu.blog.common.annotation.Controller
import dev.yidafu.blog.common.annotation.Get
import dev.yidafu.blog.themes.PageNames
import io.ktor.server.application.*
import io.ktor.server.response.*
import org.mapstruct.factory.Mappers
import org.slf4j.LoggerFactory

@Controller
class ArticleController(
  private val articleService: ArticleService,
) {
  private val log = LoggerFactory.getLogger(ArticleController::class.java)
  private val convertor = Mappers.getMapper(ArticleConvertor::class.java)

  @Get(Routes.ADMIN_ARTICLE_LIST)
  suspend fun articleListPage(call: ApplicationCall) {
    val pageNum = call.request.queryParameters["page"]?.toInt() ?: 1
    val pageSize = call.request.queryParameters["size"]?.toInt() ?: 10

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
    call.render(PageNames.ADMIN_ARTICLE_LIST, mapOf("pagination" to vo))
  }

  @Get(Routes.ADMIN_ARTICLE_DETAIL)
  @Get(Routes.ADMIN_ARTICLE_DETAIL_2)
  suspend fun articleDetailPage(call: ApplicationCall) {
    val id = call.parameters["id"]?.toIntOrNull() ?: 0
    val model = articleService.getById(id)
    if (model == null) {
      call.respondRedirect("/404")
      return
    }
    val vo = convertor.toVO(model)
    call.render(PageNames.ADMIN_ARTICLE_DETAIL, mapOf("article" to vo))
  }

  @Get(Routes.ADMIN_ARTICLE_HISTORY)
  suspend fun articleHistoryListPage(call: ApplicationCall) {
    val id = call.parameters["id"]?.toIntOrNull() ?: 0
    val model = articleService.getById(id)
    if (model == null) {
      call.respondRedirect("/404")
      return
    }
    val vo = convertor.toVO(model)
    call.respondText("Rendering page: ${PageNames.ADMIN_ARTICLE_DETAIL} with data: $vo")
  }

  @Get(Routes.ADMIN_ARTICLE_STATISTIC)
  suspend fun articleStatisticPage(call: ApplicationCall) {
    val id = call.parameters["id"]?.toIntOrNull() ?: 0
    val model = articleService.getById(id)
    if (model == null) {
      call.respondRedirect("/404")
      return
    }
    val vo = convertor.toVO(model)
    call.respondText("Rendering page: ${PageNames.ADMIN_ARTICLE_DETAIL} with data: $vo")
  }
}
