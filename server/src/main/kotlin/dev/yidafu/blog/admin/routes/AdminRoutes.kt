package dev.yidafu.blog.admin.routes

import dev.yidafu.blog.common.Routes
import dev.yidafu.blog.admin.handler.AdminHandler
import dev.yidafu.blog.common.handler.CommonHandler
import dev.yidafu.blog.admin.handler.ConfigurationHandler
import io.vertx.core.http.HttpMethod
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.BodyHandler
import io.vertx.kotlin.coroutines.CoroutineRouterSupport
import org.koin.core.Koin

fun CoroutineRouterSupport.mountAdminRoutes(router: Router, koin: Koin) {

  val adminHandler = koin.get<AdminHandler>()
  val commonHandler = koin.get<CommonHandler>()
  val configHandler = koin.get<ConfigurationHandler>()
  router.route().method(HttpMethod.POST).handler(BodyHandler.create())
  router.route()
    .handler(commonHandler::localHandler)
    .coHandler(requestHandler = commonHandler::initConfiguration)
  router.get(Routes.ADMIN_URL)
    .coHandler(requestHandler = adminHandler::indexPage)

  router.get(Routes.CONFIGURATION_URL).coHandler(requestHandler = adminHandler::configPage)

  router.post(Routes.CONFIGURATION_URL).coHandler(requestHandler = configHandler::updateConfigAction)

  router.get(Routes.CONFIG_APPEARANCE_URL).coHandler(requestHandler = configHandler::appearancePage)
  router.get(Routes.CONFIG_SYNC_URL).coHandler(requestHandler = configHandler::synchronousPage)
  router.get(Routes.CONFIG_DATA_SOURCE_URL).coHandler(requestHandler = configHandler::dataSourcePage)

  router.get(Routes.PICTURES_URL).coHandler(requestHandler = adminHandler::picturesPage)
}
