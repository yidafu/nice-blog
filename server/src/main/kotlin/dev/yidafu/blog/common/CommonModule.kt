package dev.yidafu.blog.common

import io.ktor.http.HttpHeaders
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.callid.CallId
import io.ktor.server.plugins.calllogging.CallLogging
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respondText

suspend inline fun Application.commonModule() {
//  injectAllDependencies()
  install(CallId) {
    header(HttpHeaders.XRequestId)
    verify { callId: String ->
      callId.isNotEmpty()
    }
  }

  install(CallLogging)

  install(StatusPages) {
    exception<Throwable> { call, cause ->
//      logger.error("Error in admin module", cause)
      call.respondText("Internal Server Error", status = io.ktor.http.HttpStatusCode.InternalServerError)
    }
  }

//  createRoutes1()
}
