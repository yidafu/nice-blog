package dev.yidafu.blog.common.routes

import dev.yidafu.blog.common.ext.render
import dev.yidafu.blog.themes.PageNames
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.mountErrorRoutes() {
  routing {
    get("/400") {
      call.render(PageNames.ERROR_400)
    }

    get("/401") {
      call.render(PageNames.ERROR_401)
    }

    get("/403") {
      call.render(PageNames.ERROR_403)
    }

    get("/404") {
      call.render(PageNames.ERROR_404)
    }

    get("/405") {
      call.render(PageNames.ERROR_405)
    }

    get("/408") {
      call.render(PageNames.ERROR_408)
    }

    get("/429") {
      call.render(PageNames.ERROR_429)
    }

    get("/500") {
      call.render(PageNames.ERROR_500)
    }

    get("/502") {
      call.render(PageNames.ERROR_502)
    }

    get("/503") {
      call.render(PageNames.ERROR_503)
    }

    get("/504") {
      call.render(PageNames.ERROR_504)
    }
  }
}

