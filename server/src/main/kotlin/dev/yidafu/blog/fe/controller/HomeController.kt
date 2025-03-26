package dev.yidafu.blog.fe.controller

import dev.yidafu.blog.common.Routes
import dev.yidafu.blog.common.converter.ArticleConvertor
import dev.yidafu.blog.common.ext.html
import dev.yidafu.blog.common.vo.ArticleListVO
import dev.yidafu.blog.fe.handler.HomePageHandler
import dev.yidafu.blog.fe.service.ArticleService
import dev.yidafu.blog.fe.views.pages.ArticleDetailPage
import dev.yidafu.blog.fe.views.pages.ArticleListPage
import dev.yidafu.blog.ksp.annotation.Controller
import dev.yidafu.blog.ksp.annotation.Get
import io.vertx.ext.web.RoutingContext
import org.koin.core.annotation.Single
import org.mapstruct.factory.Mappers
import org.slf4j.LoggerFactory

@Controller
@Single
class HomeController(
  private val articleService: ArticleService,
) {
  private val log = LoggerFactory.getLogger(HomePageHandler::class.java)
  private val articleConvertor = Mappers.getMapper(ArticleConvertor::class.java)

  @Get(Routes.ROOT_URL)
  @Get(Routes.ARTICLE_LIST)
  suspend fun indexPage(ctx: RoutingContext) {
    val list = articleService.getAll()
    val voList = articleConvertor.toVO(list)
    ctx.html(ArticleListPage::class.java, ArticleListVO(voList))
  }

  @Get(Routes.ARTICLE_DETAIL)
  suspend fun articlePage(ctx: RoutingContext) {
    val id = ctx.pathParam("identifier")
    val article = articleService.getOneByIdentifier(id)
    article?.let {
      val vo = articleConvertor.toVO(it)
      ctx.html(ArticleDetailPage::class.java, vo)
    } ?: run {
      ctx.redirect("/404")
    }
  }
}
