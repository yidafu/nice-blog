package dev.yidafu.blog.fe

import dev.yidafu.blog.common.db.ExposedDatabase
import dev.yidafu.blog.generated.fe.controller.createHomeControllerRoute
import dev.yidafu.blog.generated.injectAllDependencies
import io.github.flaxoos.ktor.server.plugins.ratelimiter.RateLimiting
import io.github.flaxoos.ktor.server.plugins.ratelimiter.implementations.TokenBucket
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.compression.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.slf4j.LoggerFactory
import kotlin.time.Duration.Companion.seconds

suspend fun Application.frontendModule() {
  injectAllDependencies()
  val logger = LoggerFactory.getLogger("admin")

  ExposedDatabase.init()

  ExposedDatabase.createTables()

  install(StatusPages) {
    exception<Throwable> { call, cause ->
      call.respondText(
        text = "Internal Server Error: ${cause.message}",
        status = HttpStatusCode.InternalServerError,
      )
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
