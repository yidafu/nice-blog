package dev.yidafu.blog.common.routes

import dev.yidafu.blog.common.BlogConfig
import dev.yidafu.blog.common.Routes
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.FileSystemAccess
import io.vertx.ext.web.handler.StaticHandler
import io.vertx.kotlin.coroutines.CoroutineRouterSupport

fun CoroutineRouterSupport.mountPublicRoutes(router: Router) {
  router.route(Routes.PUBLIC_URL).handler(StaticHandler.create("public"))
  router.get(Routes.UPLOAD_URL).handler(StaticHandler.create(FileSystemAccess.ROOT, BlogConfig.DEFAULT_UPLOAD_DIRECTORY))
}
