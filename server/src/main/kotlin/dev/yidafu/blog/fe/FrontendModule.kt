package dev.yidafu.blog.fe

import dev.yidafu.blog.common.TemplateManagerLoader
import dev.yidafu.blog.common.db.ExposedDatabase
import dev.yidafu.blog.common.plugins.configureGlobalConfiguration
import dev.yidafu.blog.common.plugins.configureLocaleHandler
import dev.yidafu.blog.common.plugins.configureStatusPages
import dev.yidafu.blog.common.routes.mountErrorRoutes
import dev.yidafu.blog.common.routes.mountPublicRoutes
import dev.yidafu.blog.generated.fe.controller.createHomeControllerRoute
import dev.yidafu.blog.generated.injectAllDependencies
import io.github.flaxoos.ktor.server.plugins.ratelimiter.RateLimiting
import io.github.flaxoos.ktor.server.plugins.ratelimiter.implementations.TokenBucket
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.callid.CallId
import io.ktor.server.plugins.calllogging.CallLogging
import io.ktor.server.plugins.compression.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.slf4j.LoggerFactory
import kotlin.time.Duration.Companion.seconds

suspend fun Application.frontendModule() {
  // 依赖注入（每个 Application 实例需要独立注入）
  injectAllDependencies()

  // 注意：数据库、模板已在 Application.kt 中统一初始化
  val logger = LoggerFactory.getLogger("frontend")

  // 安装全局拦截器
  configureGlobalConfiguration()
  configureLocaleHandler()

  // 挂载静态资源路由
  mountPublicRoutes()

  // 挂载错误页面路由
  mountErrorRoutes()

  // 配置统一的错误页面处理
  configureStatusPages()

  install(CallLogging)

  install(CallId) {
    header(HttpHeaders.XRequestId)
    verify { callId: String ->
      callId.isNotEmpty()
    }
  }
  // 配置压缩
  install(Compression) {
    gzip { }
    deflate { }
  }
  routing {
    route("/") {
      install(RateLimiting) {
        rateLimiter {
          type = TokenBucket::class
          rate = 1.seconds
          capacity = 100
        }
        whiteListedHosts = setOf()
        blackListedAgents = setOf("malicious-agent")
        blackListedCallerCallHandler = { call ->
          call.respond(HttpStatusCode.Forbidden, "You are blacklisted and cannot access the API.")
        }
      }
    }
  }

  createHomeControllerRoute()
}
