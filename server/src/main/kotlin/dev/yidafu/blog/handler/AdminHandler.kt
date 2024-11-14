package dev.yidafu.blog.handler

import io.vertx.ext.web.RoutingContext
import org.koin.core.annotation.Single

@Single
class AdminHandler {
  suspend fun indexPage(ctx: RoutingContext) {
    ctx.end("<h1>Admin Page</h1>")
  }
}
