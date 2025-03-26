package dev.yidafu.blog.admin.routes

import dev.yidafu.blog.admin.controller.AuthController
import dev.yidafu.blog.admin.controller.createRoutes
import dev.yidafu.blog.common.Routes
import dev.yidafu.blog.common.handler.CommonHandler
import io.vertx.core.Vertx
import io.vertx.core.http.HttpMethod
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.BodyHandler
import io.vertx.ext.web.handler.SessionHandler
import io.vertx.ext.web.sstore.LocalSessionStore
import io.vertx.kotlin.coroutines.CoroutineRouterSupport
import org.koin.core.Koin

fun CoroutineRouterSupport.mountAdminRoutes(
  router: Router,
  koin: Koin,
  vertx: Vertx,
) {
  val commonHandler = koin.get<CommonHandler>()
  val authHandler = koin.get<AuthController>()

  val sessionHandler =
    SessionHandler.create(LocalSessionStore.create(vertx))
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

  createRoutes(router)
}
