package dev.yidafu.blog.fe

import dev.yidafu.blog.common.routes.mountPublicRoutes
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.LoggerHandler
import io.vertx.kotlin.coroutines.CoroutineRouterSupport
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.coAwait
import org.koin.core.Koin
import org.slf4j.LoggerFactory
import dev.yidafu.blog.common.controller.createRoutes as createCommonRouter
import dev.yidafu.blog.fe.controller.createRoutes as createFeRouter

class FrontendVerticle(private val koin: Koin) : CoroutineVerticle(), CoroutineRouterSupport {
  private val log = LoggerFactory.getLogger(FrontendVerticle::class.java)

  override suspend fun start() {
    super.start()

    try {
      val server = vertx.createHttpServer()
      val router = Router.router(vertx)
      router.route().handler(LoggerHandler.create())

      mountPublicRoutes(router)
      createCommonRouter(router)
      createFeRouter(router)

      router.errorHandler(404) { ctx ->
        ctx.end("<h1>404 Not Found</h1>")
      }

      server
        .requestHandler(router)
        .listen(8080).coAwait()
      println("HTTP Frontend server started on port 8080")
    } catch (e: Exception) {
      log.error("frontend verticle", e)
    }
  }
}
