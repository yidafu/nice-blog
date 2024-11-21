package dev.yidafu.blog.routes

import dev.yidafu.blog.handler.HomePageHandler
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.StaticHandler
import io.vertx.kotlin.coroutines.CoroutineRouterSupport
import org.koin.core.Koin

fun CoroutineRouterSupport.mountPublicRoutes(router: Router) {

  router.route("/public/*").handler(StaticHandler.create("public"))
}
