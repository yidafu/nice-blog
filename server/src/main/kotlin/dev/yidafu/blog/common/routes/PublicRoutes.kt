package dev.yidafu.blog.common.routes

import dev.yidafu.blog.common.BlogConfig
import dev.yidafu.blog.common.Routes
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*
import java.io.File

fun Application.mountPublicRoutes() {
  routing {
    staticResources(Routes.PUBLIC_URL, "public") {
      enableAutoHeadResponse()
      preCompressed(CompressedFileType.BROTLI, CompressedFileType.GZIP)
    }
    staticFiles(Routes.UPLOAD_URL, File(BlogConfig.DEFAULT_UPLOAD_DIRECTORY))
  }
}
