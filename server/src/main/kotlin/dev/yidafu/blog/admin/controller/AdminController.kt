package dev.yidafu.blog.admin.controller

import dev.yidafu.blog.common.Routes
import dev.yidafu.blog.common.annotation.Controller
import dev.yidafu.blog.common.annotation.Get
import io.ktor.server.application.*
import io.ktor.server.response.*
import org.slf4j.LoggerFactory

@Controller
class AdminController {
  private val log = LoggerFactory.getLogger(AdminController::class.java)

  @Get(Routes.ADMIN_URL)
  suspend fun indexPage(call: ApplicationCall) {
    call.respondRedirect(Routes.ADMIN_DASHBOARD_URL)
  }

  @Get(Routes.CONFIGURATION_URL)
  suspend fun configPage(call: ApplicationCall) {
    call.respondRedirect(Routes.CONFIG_APPEARANCE_URL)
  }

  @Get(Routes.PICTURES_URL)
  suspend fun picturesPage(call: ApplicationCall) {
    call.respondText("403")
  }
}
