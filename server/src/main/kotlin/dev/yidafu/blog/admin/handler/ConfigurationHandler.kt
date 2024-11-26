package dev.yidafu.blog.admin.handler

import de.comahe.i18n4k.Locale
import dev.yidafu.blog.common.bean.bo.ConfigurationBO
import dev.yidafu.blog.common.bean.dto.ConfigurationDTO
import dev.yidafu.blog.common.bean.vo.AdminAppearanceVo
import dev.yidafu.blog.common.bean.vo.AdminDataSourceVO
import dev.yidafu.blog.common.bean.vo.AdminSynchronousVO
import dev.yidafu.blog.common.ConfigurationKeys
import dev.yidafu.blog.common.ConstantKeys
import dev.yidafu.blog.common.FormKeys
import dev.yidafu.blog.common.Routes
import dev.yidafu.blog.common.ext.html
import dev.yidafu.blog.common.handler.CommonHandler
import dev.yidafu.blog.common.modal.ConfigurationModal
import dev.yidafu.blog.admin.services.ConfigurationService
import dev.yidafu.blog.admin.views.pages.AdminConfigAppearancePage
import dev.yidafu.blog.admin.views.pages.AdminConfigDataSourcePage
import dev.yidafu.blog.admin.views.pages.AdminConfigSyncPage
import io.vertx.core.http.HttpHeaders
import io.vertx.ext.web.RoutingContext
import org.koin.core.annotation.Single
import org.slf4j.LoggerFactory

@Single
class ConfigurationHandler(
  private val configService: ConfigurationService,

  ) {
  private val log = LoggerFactory.getLogger(AdminHandler::class.java)

  suspend fun appearancePage(ctx: RoutingContext) {
    val local = ctx.get<Locale>(ConstantKeys.LANGUAGE_CONTEXT)

    val configBo = ctx.get<ConfigurationBO>(CommonHandler.GLOBAL_CONFIGURATION)
    val vo = AdminAppearanceVo(
      local,
      ctx.normalizedPath(),
      configBo.siteTitle,
      configBo.githubUrl,
    )
    ctx.html(AdminConfigAppearancePage(vo).render())
  }

  /**
   * 通用更新配置
   */
  suspend fun updateConfigAction(ctx: RoutingContext) {
    log.info("updateAppearancePage")
    val req = ctx.request()
    val body = req.formAttributes()
    val referer = req.getHeader(HttpHeaders.REFERER) ?: Routes.CONFIGURATION_URL


    val dtoList = listOf(
      FormKeys.SITE_TITLE to ConfigurationKeys.SITE_TITLE,
      FormKeys.GITHUB_URL to ConfigurationKeys.GITHUB_URL,
      FormKeys.CRON_EXPR to ConfigurationKeys.SYNC_CRON_EXPR,
      FormKeys.SOURCE_TYPE to ConfigurationKeys.SOURCE_TYPE,
      FormKeys.SOURCE_URL to ConfigurationKeys.SOURCE_URL,
      FormKeys.SOURCE_TOKEN to ConfigurationKeys.SOURCE_TOKEN,
    ).mapNotNull { keyPair ->
      body.get(keyPair.first)?.let { value ->
        ConfigurationDTO(keyPair.second, value)
      }
    }

    log.info("开始更新配置 ${dtoList.joinToString(",")}}")
    configService.updateConfig(dtoList)
    ctx.redirect(referer)
  }

  suspend fun synchronousPage(ctx: RoutingContext) {
    val local = ctx.get<Locale>(ConstantKeys.LANGUAGE_CONTEXT)

    val configBo = ctx.get<ConfigurationBO>(CommonHandler.GLOBAL_CONFIGURATION)
    val config = configService.getByKey(ConfigurationKeys.SYNC_CRON_EXPR)
    val vo = AdminSynchronousVO(
      config.configValue,
      local,
      ctx.normalizedPath(),
      configBo.siteTitle,
      configBo.githubUrl,
    )
    ctx.html(AdminConfigSyncPage(vo).render())
  }

  suspend fun dataSourcePage(ctx: RoutingContext) {
    val local = ctx.get<Locale>(ConstantKeys.LANGUAGE_CONTEXT)
    val configBo = ctx.get<ConfigurationBO>(CommonHandler.GLOBAL_CONFIGURATION)
    val configs = configService.getByKeys(listOf(
      ConfigurationKeys.SOURCE_TYPE,
      ConfigurationKeys.SOURCE_URL,
      ConfigurationKeys.SOURCE_TOKEN,
    ))

    val vo = AdminDataSourceVO(
      configs.getByKey(ConfigurationKeys.SOURCE_TYPE) ?: "",
      configs.getByKey(ConfigurationKeys.SOURCE_URL) ?: "",
      configs.getByKey(ConfigurationKeys.SOURCE_TOKEN) ?: "",
      local,
      ctx.normalizedPath(),
      configBo.siteTitle,
      configBo.githubUrl,
    )

    ctx.html(AdminConfigDataSourcePage(vo).render())

  }

  fun List<ConfigurationModal>.getByKey(key: String): String? {
    return find { it.configKey == key }?.configValue
  }
}
