package dev.yidafu.blog.routes

import dev.yidafu.blog.handler.HomePageHandler
import io.vertx.ext.web.Router
import io.vertx.kotlin.coroutines.CoroutineRouterSupport
import org.koin.core.Koin
import org.koin.core.KoinApplication


fun CoroutineRouterSupport.mountPostRoutes(router: Router, koin: Koin) {

  val homeHandler = koin.get<HomePageHandler>()

  router.get("/")
    .coHandler(requestHandler =  homeHandler::indexPage)

  router.get("/posts").coHandler(requestHandler =  homeHandler::indexPage)
}
