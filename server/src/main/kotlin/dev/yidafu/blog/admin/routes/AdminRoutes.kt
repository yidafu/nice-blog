package dev.yidafu.blog.admin.routes

import dev.yidafu.blog.common.Routes
import dev.yidafu.blog.admin.handler.AdminHandler
import dev.yidafu.blog.admin.handler.AuthHandler
import dev.yidafu.blog.common.handler.CommonHandler
import dev.yidafu.blog.admin.handler.ConfigurationHandler
import dev.yidafu.blog.admin.handler.SynchronousHandler
import io.vertx.core.Vertx
import io.vertx.core.http.HttpMethod
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.BodyHandler
import io.vertx.ext.web.handler.SessionHandler

import io.vertx.ext.web.sstore.LocalSessionStore
import io.vertx.kotlin.coroutines.CoroutineRouterSupport
import org.koin.core.Koin

fun CoroutineRouterSupport.mountAdminRoutes(router: Router, koin: Koin, vertx: Vertx) {

  val adminHandler = koin.get<AdminHandler>()
  val commonHandler = koin.get<CommonHandler>()
  val configHandler = koin.get<ConfigurationHandler>()
  val syncHandler = koin.get<SynchronousHandler>()
  val authHandler = koin.get<AuthHandler>()

  val sessionHandler = SessionHandler.create(LocalSessionStore.create(vertx))
    .setCookieHttpOnlyFlag(true)
  // common routes
  router.route().method(HttpMethod.POST).handler(BodyHandler.create())
  router.route().handler(sessionHandler)
  router.route()
    .handler(commonHandler::localHandler)
    .coHandler(requestHandler = commonHandler::initConfiguration)

  // login routes
  router.get(Routes.LOGIN_URL)
    .coHandler(requestHandler = authHandler::genRsaKeyPair)
    .coHandler(requestHandler = authHandler::loginPage)
  router.post(Routes.LOGIN_URL).coHandler(requestHandler = authHandler::loginAction)
  router.get(Routes.LOGOUT_URL).coHandler(requestHandler = authHandler::logoutAction)

  router.get(Routes.ADMIN_URL + "/*").coHandler(requestHandler = authHandler::checkLoginAction)
  router.get(Routes.ADMIN_URL)
    .coHandler(requestHandler = adminHandler::indexPage)

  router.get(Routes.CONFIGURATION_URL).coHandler(requestHandler = adminHandler::configPage)

  router.post(Routes.CONFIGURATION_URL).coHandler(requestHandler = configHandler::updateConfigAction)

  router.get(Routes.CONFIG_APPEARANCE_URL).coHandler(requestHandler = configHandler::appearancePage)
  router.get(Routes.CONFIG_SYNC_URL).coHandler(requestHandler = configHandler::synchronousPage)
  router.get(Routes.CONFIG_DATA_SOURCE_URL).coHandler(requestHandler = configHandler::dataSourcePage)

  router.get(Routes.SYNC_URL).coHandler(requestHandler = syncHandler::syncPage)
  router.get(Routes.SYNC_LOG_URL).coHandler(requestHandler = syncHandler::syncLogListPage)
  router.get(Routes.SYNC_OPERATE_URL).coHandler(requestHandler =  syncHandler::syncOperatePage)
  router.get(Routes.SYNC_API_START_URL)
    .coHandler(requestHandler =  syncHandler::startSync)
  router.get(Routes.SYNC_API_LOG_URL)
    .coHandler(requestHandler = syncHandler::getSyncLog)
  router.get(Routes.SYNC_LOG_DETAIL_URL).coHandler(requestHandler = syncHandler::syncLogDetailPage)

  router.get(Routes.PICTURES_URL).coHandler(requestHandler = adminHandler::picturesPage)
}
