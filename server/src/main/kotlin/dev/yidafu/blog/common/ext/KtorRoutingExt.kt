package dev.yidafu.blog.common.ext

import io.ktor.server.routing.*

fun Route.any(
  path: String,
  body: RoutingHandler,
): Route {
  return route(path) { handle(body) }
}
