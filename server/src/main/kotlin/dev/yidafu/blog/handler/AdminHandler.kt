package dev.yidafu.blog.handler

import de.comahe.i18n4k.Locale
import dev.yidafu.blog.common.ConstantKeys
import dev.yidafu.blog.common.Routes
import dev.yidafu.blog.bean.vo.AdminBaseVo
import dev.yidafu.blog.ext.html
import dev.yidafu.blog.service.ConfigurationService
import dev.yidafu.blog.views.layouts.AdminLayout
import io.vertx.ext.web.RoutingContext
import kotlinx.html.h1
import org.koin.core.annotation.Single
import org.slf4j.LoggerFactory

@Single
class AdminHandler(
  private val configService: ConfigurationService,
) {
  private val log = LoggerFactory.getLogger(AdminHandler::class.java)

  suspend fun indexPage(ctx: RoutingContext) {
    val local = ctx.get<Locale>(ConstantKeys.LANGUAGE_CONTEXT)
    val vo = AdminBaseVo(
      local,
      ctx.normalizedPath(),
      "Dov Yih",
      "https://github.com/yidafu",
    )
    ctx.html(AdminLayout(vo).layout {
      h1 {
        +"Admin Page"
      }
    })
  }


  fun configPage(ctx: RoutingContext) {
    ctx.redirect(Routes.CONFIG_APPEARANCE_URL)
  }

  suspend fun picturesPage(ctx: RoutingContext) {
    val local = ctx.get<Locale>(ConstantKeys.LANGUAGE_CONTEXT)

    val vo = AdminBaseVo(
      local,
      ctx.normalizedPath(),
      "Dov Yih",
      "https://github.com/yidafu",
    )
    ctx.html(AdminLayout(vo).layout {
      h1 {
        +"Pictures Page"
      }
    })
  }
}
