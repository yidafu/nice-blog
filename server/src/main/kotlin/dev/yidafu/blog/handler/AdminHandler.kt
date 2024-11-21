package dev.yidafu.blog.handler

import de.comahe.i18n4k.Locale
import dev.yidafu.blog.common.ConstantKeys
import dev.yidafu.blog.common.Routes
import dev.yidafu.blog.bean.bo.ConfigurationBO
import dev.yidafu.blog.bean.dto.ConfigurationDTO
import dev.yidafu.blog.bean.vo.AdminAppearanceVo
import dev.yidafu.blog.bean.vo.AdminBaseVo
import dev.yidafu.blog.common.ConfigurationKeys
import dev.yidafu.blog.common.FormKeys
import dev.yidafu.blog.ext.html
import dev.yidafu.blog.service.ConfigurationService
import dev.yidafu.blog.views.layouts.AdminLayout
import dev.yidafu.blog.views.pages.AdminAppearancePage
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

  suspend fun appearancePage(ctx: RoutingContext) {
    val local = ctx.get<Locale>(ConstantKeys.LANGUAGE_CONTEXT)

//    val body = ctx.body().asJsonObject()
//    log.info("form body {}", body)
    val configBo = ctx.get<ConfigurationBO>(CommonHandler.GLOBAL_CONFIGURATION)
    val vo = AdminAppearanceVo(
      local,
      ctx.normalizedPath(),
      configBo.siteTitle,
      configBo.githubUrl,
    )
    ctx.html(AdminAppearancePage(ctx).render(vo))
  }


  suspend fun updateAppearancePage(ctx: RoutingContext) {

    log.info("updateAppearancePage")

    val body = ctx.request().formAttributes()

    val dtoList = mutableListOf<ConfigurationDTO>()
    body.get(FormKeys.SITE_TITLE)?.let {
      log.info("{} ==> {}", ConfigurationKeys.SITE_TITLE , it)
      dtoList.add(ConfigurationDTO(ConfigurationKeys.SITE_TITLE, it))
    }
    body.get(FormKeys.GITHUB_URL)?.let {
      log.info("{} ==> {}", ConfigurationKeys.GITHUB_URL , it)
      dtoList.add(ConfigurationDTO(ConfigurationKeys.GITHUB_URL, it))
    }
    log.info("开始更新配置")
    configService.updateConfig(dtoList)

    ctx.redirect(Routes.APPEARANCE_URL)
  }


  suspend fun configurationPage(ctx: RoutingContext) {
    val local = ctx.get<Locale>(ConstantKeys.LANGUAGE_CONTEXT)

    val vo = AdminBaseVo(
      local,
      ctx.normalizedPath(),
      "Dov Yih",
      "https://github.com/yidafu",
    )
    ctx.html(AdminLayout(vo).layout {
      h1 {
        +"Configuration Page"
      }
    })
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
