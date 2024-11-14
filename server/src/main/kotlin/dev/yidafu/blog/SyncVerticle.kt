package dev.yidafu.blog

import dev.yidafu.blog.routes.mountSyncRoutes
import io.vertx.ext.web.Router
import io.vertx.kotlin.coroutines.CoroutineRouterSupport
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.coAwait
import org.koin.core.Koin

class SyncVerticle(private val koin: Koin): CoroutineVerticle(), CoroutineRouterSupport {
  override suspend fun start() {
    super.start()

    val server = vertx.createHttpServer()
    val router = Router.router(vertx)
    mountSyncRoutes(router, koin)
    router.errorHandler(404) {ctx ->
      ctx.end("<h1>404 Not Found</h1>")
    }
    server
      .requestHandler(router)
      .listen(8081).coAwait()
    println("HTTP Sync server started on port 8081")
  }
}
