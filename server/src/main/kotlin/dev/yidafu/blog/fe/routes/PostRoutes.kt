package dev.yidafu.blog.fe.routes

import dev.yidafu.blog.fe.handler.HomePageHandler
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.StaticHandler
import io.vertx.kotlin.coroutines.CoroutineRouterSupport
import org.koin.core.Koin


fun CoroutineRouterSupport.mountPostRoutes(router: Router, koin: Koin) {

  val homeHandler = koin.get<HomePageHandler>()
  router.route("/public/*").handler(StaticHandler.create("public"))
  router.get("/")
    .coHandler(requestHandler =  homeHandler::indexPage)

  router.get("/posts").coHandler(requestHandler =  homeHandler::indexPage)
}
