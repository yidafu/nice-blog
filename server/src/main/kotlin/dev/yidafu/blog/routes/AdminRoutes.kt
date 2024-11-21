package dev.yidafu.blog.routes

import dev.yidafu.blog.common.Routes
import dev.yidafu.blog.handler.AdminHandler
import dev.yidafu.blog.handler.CommonHandler
import io.vertx.core.http.HttpMethod
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.BodyHandler
import io.vertx.kotlin.coroutines.CoroutineRouterSupport
import org.koin.core.Koin

fun CoroutineRouterSupport.mountAdminRoutes(router: Router, koin: Koin) {

  val adminHandler = koin.get<AdminHandler>()
  val commonHandler = koin.get<CommonHandler>()
  router.route().method(HttpMethod.POST).handler(BodyHandler.create())
  router.route()
    .handler(commonHandler::localHandler)
    .coHandler(requestHandler = commonHandler::initConfiguration)
  router.get("/admin")
    .coHandler(requestHandler = adminHandler::indexPage)


  router.get(Routes.APPEARANCE_URL).coHandler(requestHandler = adminHandler::appearancePage)
  router.post(Routes.APPEARANCE_URL).coHandler(requestHandler = adminHandler::updateAppearancePage)

  router.get(Routes.CONFIGURATION_URL).coHandler(requestHandler = adminHandler::configurationPage)
  router.get(Routes.PICTURES_URL).coHandler(requestHandler = adminHandler::picturesPage)
}
