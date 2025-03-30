package dev.yidafu.blog.admin.controller

import de.comahe.i18n4k.Locale
import dev.yidafu.blog.admin.views.pages.PicturePage
import dev.yidafu.blog.common.ConstantKeys
import dev.yidafu.blog.common.Routes
import dev.yidafu.blog.common.ext.html
import dev.yidafu.blog.common.vo.PageVO
import dev.yidafu.blog.ksp.annotation.Controller
import dev.yidafu.blog.ksp.annotation.Get
import io.vertx.ext.web.RoutingContext
import org.koin.core.annotation.Single
import org.slf4j.LoggerFactory

@Controller
@Single
class AdminController {
  private val log = LoggerFactory.getLogger(AdminController::class.java)

  @Get(Routes.ADMIN_URL)
  suspend fun indexPage(ctx: RoutingContext) {
    val local = ctx.get<Locale>(ConstantKeys.LANGUAGE_CONTEXT)

    ctx.redirect(Routes.CONFIG_APPEARANCE_URL)
  }

  @Get(Routes.CONFIGURATION_URL)
  fun configPage(ctx: RoutingContext) {
    ctx.redirect(Routes.CONFIG_APPEARANCE_URL)
  }

  @Get(Routes.PICTURES_URL)
  suspend fun picturesPage(ctx: RoutingContext) {
    val vo = PageVO()
    ctx.html(PicturePage::class.java, vo)
  }
}
