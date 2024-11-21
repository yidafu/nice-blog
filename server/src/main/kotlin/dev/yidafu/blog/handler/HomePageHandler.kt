package dev.yidafu.blog.handler

import dev.yidafu.blog.ext.html
import dev.yidafu.blog.service.ArticleService
import dev.yidafu.blog.views.layouts.BaseLayout
import io.vertx.ext.web.RoutingContext
import kotlinx.html.h1
import org.koin.core.annotation.Single
import org.slf4j.LoggerFactory

@Single
class HomePageHandler(
  private val articleService: ArticleService
)  {
  private val log = LoggerFactory.getLogger(HomePageHandler::class.java)

  suspend fun indexPage(ctx: RoutingContext) {
    log.info("首页请求")

    ctx.html(BaseLayout().layout {
      h1 { +"Index Page!" }
    })
  }
}
