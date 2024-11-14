package dev.yidafu.blog.handler

import dev.yidafu.blog.service.ArticleService
import io.vertx.ext.web.RoutingContext
import org.koin.core.annotation.Single
import org.slf4j.LoggerFactory

@Single
class HomePageHandler(
  private val articleService: ArticleService
)  {
  private val log = LoggerFactory.getLogger(HomePageHandler::class.java)

  suspend fun indexPage(ctx: RoutingContext) {
    log.info("首页请求")
    val list = articleService.getAll()
    ctx.json(list)
  }
}
