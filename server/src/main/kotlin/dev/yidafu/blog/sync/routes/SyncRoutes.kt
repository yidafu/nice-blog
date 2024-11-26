package dev.yidafu.blog.sync.routes

import dev.yidafu.blog.sync.handler.SyncApiHandler
import io.vertx.ext.web.Router
import io.vertx.kotlin.coroutines.CoroutineRouterSupport
import org.koin.core.Koin


fun CoroutineRouterSupport.mountSyncRoutes(router: Router, koin: Koin) {

  val syncHandler = koin.get<SyncApiHandler>()

  router.get("/sync/start")
    .coHandler(requestHandler =  syncHandler::startSync)

  router.get("/sync/log/:id")
    .coHandler(requestHandler = syncHandler::getSyncLog)
}
