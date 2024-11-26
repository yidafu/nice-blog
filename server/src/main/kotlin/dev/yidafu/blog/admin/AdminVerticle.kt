package dev.yidafu.blog.admin

import dev.yidafu.blog.admin.routes.mountAdminRoutes
import dev.yidafu.blog.common.routes.mountPublicRoutes
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.LoggerHandler
import io.vertx.kotlin.coroutines.CoroutineRouterSupport
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.coAwait
import org.koin.core.Koin

class AdminVerticle(private val koin: Koin): CoroutineVerticle(), CoroutineRouterSupport {
  override suspend fun start() {
    super.start()

    val server = vertx.createHttpServer()
    val router = Router.router(vertx)
    router.route().handler(LoggerHandler.create())

    mountAdminRoutes(router, koin)
    mountPublicRoutes(router)

    router.errorHandler(404) {ctx ->
      ctx.end("<h1>404 Not Found</h1>")
    }
    server
      .requestHandler(router)
      .listen(8081).coAwait()
    println("HTTP Admin server started on port 8081")
  }
}
