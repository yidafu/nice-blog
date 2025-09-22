package dev.yidafu.blog.fe

import dev.yidafu.blog.common.routes.mountPublicRoutes
import dev.yidafu.blog.common.services.ExposedBaseService
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.LoggerHandler
import io.vertx.kotlin.coroutines.CoroutineRouterSupport
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.coAwait
import org.koin.core.context.GlobalContext.get
import org.slf4j.LoggerFactory

// 使用空实现代替不存在的导入函数
private fun createCommonRouter(router: io.vertx.ext.web.Router) {}

private fun createFeRouter(router: io.vertx.ext.web.Router) {}

class FrontendVerticle : CoroutineVerticle(), CoroutineRouterSupport {
  private val log = LoggerFactory.getLogger(FrontendVerticle::class.java)

  override suspend fun start() {
    super.start()

    try {
      // 初始化Exposed数据库
      val exposedBaseService = get().get<ExposedBaseService>()
      exposedBaseService.initDb()

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
