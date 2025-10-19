package dev.yidafu.blog.common.plugins

import dev.yidafu.blog.common.ext.render
import dev.yidafu.blog.themes.PageNames
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import org.slf4j.LoggerFactory

fun Application.configureStatusPages() {
  val logger = LoggerFactory.getLogger("StatusPages")

  install(StatusPages) {
    // 异常处理 - 500
    exception<Throwable> { call, cause ->
      logger.error("Unhandled exception", cause)
      call.render(PageNames.ERROR_500)
    }

    // 4xx 客户端错误
    status(HttpStatusCode.BadRequest) { call, status ->
      call.render(PageNames.ERROR_400)
    }

    status(HttpStatusCode.Unauthorized) { call, status ->
      call.render(PageNames.ERROR_401)
    }

    status(HttpStatusCode.Forbidden) { call, status ->
      call.render(PageNames.ERROR_403)
    }

    status(HttpStatusCode.NotFound) { call, status ->
      call.render(PageNames.ERROR_404)
    }

    status(HttpStatusCode.MethodNotAllowed) { call, status ->
      call.render(PageNames.ERROR_405)
    }

    status(HttpStatusCode.RequestTimeout) { call, status ->
      call.render(PageNames.ERROR_408)
    }

    status(HttpStatusCode.TooManyRequests) { call, status ->
      call.render(PageNames.ERROR_429)
    }

    // 5xx 服务器错误
    status(HttpStatusCode.InternalServerError) { call, status ->
      call.render(PageNames.ERROR_500)
    }

    status(HttpStatusCode.BadGateway) { call, status ->
      call.render(PageNames.ERROR_502)
    }

    status(HttpStatusCode.ServiceUnavailable) { call, status ->
      call.render(PageNames.ERROR_503)
    }

    status(HttpStatusCode.GatewayTimeout) { call, status ->
      call.render(PageNames.ERROR_504)
    }
  }
}

