package dev.yidafu.blog.common.routes

import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.StaticHandler
import io.vertx.kotlin.coroutines.CoroutineRouterSupport

fun CoroutineRouterSupport.mountPublicRoutes(router: Router) {

  router.route("/public/*").handler(StaticHandler.create("public"))
}
