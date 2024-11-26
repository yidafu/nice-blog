package dev.yidafu.blog.fe.handler

import dev.yidafu.blog.common.ext.html
import dev.yidafu.blog.fe.service.ArticleService
import dev.yidafu.blog.common.views.layouts.BaseLayout
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
