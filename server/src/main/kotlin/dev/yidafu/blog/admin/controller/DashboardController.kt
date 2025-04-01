package dev.yidafu.blog.admin.controller

import dev.yidafu.blog.common.Routes
import dev.yidafu.blog.common.ext.render
import dev.yidafu.blog.common.services.ArticleService
import dev.yidafu.blog.common.vo.DashboardVO
import dev.yidafu.blog.fe.service.AccessLogService
import dev.yidafu.blog.ksp.annotation.Controller
import dev.yidafu.blog.ksp.annotation.Get
import dev.yidafu.blog.themes.PageNames
import io.vertx.ext.web.RoutingContext
import org.koin.core.annotation.Single

@Single
@Controller
class DashboardController(
  private val articleService: ArticleService,
  private val accessLogService: AccessLogService,
) {

  @Get(Routes.ADMIN_DASHBOARD_URL)
  suspend fun dashboardPage(ctx: RoutingContext) {
    val articleCount = articleService.countAll()
    val accessCount = accessLogService.countAll()
    // dashboard should
    val vo = DashboardVO(articleCount, accessCount)
    ctx.render(PageNames.ADMIN_DASHBOARD, vo)
  }
}
