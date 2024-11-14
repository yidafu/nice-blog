package dev.yidafu.blog.routes

import dev.yidafu.blog.handler.AdminHandler
import io.vertx.ext.web.Router
import io.vertx.kotlin.coroutines.CoroutineRouterSupport
import org.koin.core.Koin

fun CoroutineRouterSupport.mountAdminRoutes(router: Router, koin: Koin) {

  val adminHandler = koin.get<AdminHandler>()

  router.get("/admin")
    .coHandler(requestHandler =  adminHandler::indexPage)

}
