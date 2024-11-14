package dev.yidafu.blog.handler

import dev.yidafu.blog.common.ApiResponse
import dev.yidafu.blog.ext.kJson
import io.vertx.ext.web.RoutingContext
import org.koin.core.annotation.Single
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Single
class SyncApiHandler {
  @OptIn(ExperimentalUuidApi::class)
  suspend fun startSync(ctx: RoutingContext) {
    ctx.kJson<ApiResponse<String>>(ApiResponse.success(Uuid.random().toString()))
  }

  suspend fun getSyncLog(ctx: RoutingContext) {
    val id = ctx.pathParam("id")
    ctx.end("log $id")
  }
}
