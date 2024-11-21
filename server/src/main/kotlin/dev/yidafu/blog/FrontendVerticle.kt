package dev.yidafu.blog

import dev.yidafu.blog.routes.mountPostRoutes
import dev.yidafu.blog.routes.mountPublicRoutes
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.LoggerHandler
import io.vertx.kotlin.coroutines.CoroutineRouterSupport
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.coAwait
import org.koin.core.Koin

class FrontendVerticle(private val koin: Koin) : CoroutineVerticle(), CoroutineRouterSupport  {

  override suspend fun start() {
    super.start()


    val server = vertx.createHttpServer()
    val router = Router.router(vertx)
    router.route().handler(LoggerHandler.create())

    mountPostRoutes(router, koin)
    mountPublicRoutes(router)

    server
      .requestHandler(router)
      .listen(8080).coAwait()
    println("HTTP Frontend server started on port 8080")
  }
}
