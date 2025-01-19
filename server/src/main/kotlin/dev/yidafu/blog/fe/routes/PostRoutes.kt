package dev.yidafu.blog.fe.routes

import dev.yidafu.blog.common.Routes
import dev.yidafu.blog.common.handler.CommonHandler
import dev.yidafu.blog.fe.handler.HomePageHandler
import io.vertx.ext.web.Router
import io.vertx.kotlin.coroutines.CoroutineRouterSupport
import org.koin.core.Koin

fun CoroutineRouterSupport.mountPostRoutes(
  router: Router,
  koin: Koin,
) {
  val homeHandler = koin.get<HomePageHandler>()
  val commonHandler = koin.get<CommonHandler>()
  router.route()
    .handler(commonHandler::localHandler)
    .coHandler(requestHandler = commonHandler::initConfiguration)

  router.get("/")
    .coHandler(requestHandler = homeHandler::indexPage)

  router.get(Routes.ARTICLE_LIST).coHandler(requestHandler = homeHandler::indexPage)
  router.get(Routes.ARTICLE_DETAIL).coHandler(requestHandler = homeHandler::articlePage)
}
