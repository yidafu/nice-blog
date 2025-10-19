package dev.yidafu.blog.admin.controller

import dev.yidafu.blog.common.Routes
import dev.yidafu.blog.common.ext.render
import dev.yidafu.blog.common.services.ArticleService
import dev.yidafu.blog.fe.service.AccessLogService
import dev.yidafu.blog.common.annotation.Controller
import dev.yidafu.blog.common.annotation.Get
import dev.yidafu.blog.themes.PageNames
import io.ktor.server.application.*

@Controller
class DashboardController(
  private val articleService: ArticleService,
  private val accessLogService: AccessLogService,
) {
  @Get(Routes.ADMIN_DASHBOARD_URL)
  suspend fun dashboardPage(call: ApplicationCall) {
    val articleCount = articleService.countAll()
    val accessCount = accessLogService.countAll()

    call.render(
      PageNames.ADMIN_DASHBOARD,
      mapOf(
        "articleCount" to articleCount,
        "accessCount" to accessCount,
      ),
    )
  }
}
