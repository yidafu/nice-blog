package dev.yidafu.blog.admin.handler

import de.comahe.i18n4k.Locale
import dev.yidafu.blog.admin.views.pages.PicturePage
import dev.yidafu.blog.common.ConstantKeys
import dev.yidafu.blog.common.Routes
import dev.yidafu.blog.common.ext.html
import dev.yidafu.blog.common.services.ConfigurationService
import dev.yidafu.blog.common.vo.PageVO
import io.vertx.ext.web.RoutingContext
import org.koin.core.annotation.Single
import org.slf4j.LoggerFactory

@Single
class AdminHandler(
  private val configService: ConfigurationService,
) {
  private val log = LoggerFactory.getLogger(AdminHandler::class.java)

  suspend fun indexPage(ctx: RoutingContext) {
    val local = ctx.get<Locale>(ConstantKeys.LANGUAGE_CONTEXT)

    ctx.redirect(Routes.CONFIG_APPEARANCE_URL)
  }

  fun configPage(ctx: RoutingContext) {
    ctx.redirect(Routes.CONFIG_APPEARANCE_URL)
  }

  suspend fun picturesPage(ctx: RoutingContext) {
    val vo = PageVO()
    ctx.html(PicturePage::class.java, vo)
  }
}
