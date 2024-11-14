package dev.yidafu.blog

import dev.yidafu.blog.routes.mountPostRoutes
import io.vertx.ext.web.Router
import io.vertx.kotlin.coroutines.CoroutineRouterSupport
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.coAwait
import org.koin.core.Koin

class FrontendVerticle(private val koin: Koin) : CoroutineVerticle(), CoroutineRouterSupport  {

  override suspend fun start() {
    super.start()


    val router = Router.router(vertx)
    val server = vertx.createHttpServer()
    mountPostRoutes(router, koin)
    server
      .requestHandler(router)
      .listen(8080).coAwait()
    println("HTTP Frontend server started on port 8080")
  }
}
