package dev.yidafu.blog.admin.handler

import de.comahe.i18n4k.Locale
import dev.yidafu.blog.admin.views.pages.config.AdminConfigAppearancePage
import dev.yidafu.blog.admin.views.pages.config.AdminConfigDataSourcePage
import dev.yidafu.blog.admin.views.pages.config.AdminConfigSyncPage
import dev.yidafu.blog.common.ConfigurationKeys
import dev.yidafu.blog.common.ConstantKeys
import dev.yidafu.blog.common.FormKeys
import dev.yidafu.blog.common.Routes
import dev.yidafu.blog.common.bean.bo.ConfigurationBO
import dev.yidafu.blog.common.dto.ConfigurationDTO
import dev.yidafu.blog.common.ext.html
import dev.yidafu.blog.common.handler.CommonHandler
import dev.yidafu.blog.common.modal.ConfigurationModal
import dev.yidafu.blog.common.services.ConfigurationService
import dev.yidafu.blog.common.vo.AdminAppearanceVO
import dev.yidafu.blog.common.vo.AdminDataSourceVO
import dev.yidafu.blog.common.vo.AdminSynchronousVO
import io.vertx.core.http.HttpHeaders
import io.vertx.ext.web.RoutingContext
import org.koin.core.annotation.Single
import org.slf4j.LoggerFactory

@Single
class ConfigurationHandler(
  private val configService: ConfigurationService,

  ) {
  private val log = LoggerFactory.getLogger(ConfigurationHandler::class.java)

  suspend fun appearancePage(ctx: RoutingContext) {
    val vo = AdminAppearanceVO()
    ctx.html(AdminConfigAppearancePage::class, vo)
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
    dtoList.find { it.configKey == ConfigurationKeys.SYNC_CRON_EXPR }?.let { config ->
      log.info("send update cron expression event => ${config.configValue}")
      ctx.vertx().eventBus().send(ConstantKeys.UPDATE_CRON_EXPR, config.configValue)
    }

    log.info("start update config ${dtoList.joinToString(",")}}")
    configService.updateConfig(dtoList)
    ctx.redirect(referer)
  }

  suspend fun synchronousPage(ctx: RoutingContext) {
    val config = configService.getByKey(ConfigurationKeys.SYNC_CRON_EXPR)
    val vo = AdminSynchronousVO(
      config.configValue,
    )
    ctx.html(AdminConfigSyncPage::class, vo)
  }

  suspend fun dataSourcePage(ctx: RoutingContext) {
    val configs = configService.getByKeys(listOf(
      ConfigurationKeys.SOURCE_TYPE,
      ConfigurationKeys.SOURCE_URL,
      ConfigurationKeys.SOURCE_TOKEN,
    ))

    val vo = AdminDataSourceVO(
      configs.getByKey(ConfigurationKeys.SOURCE_TYPE) ?: "",
      configs.getByKey(ConfigurationKeys.SOURCE_URL) ?: "",
      configs.getByKey(ConfigurationKeys.SOURCE_TOKEN) ?: "",
    )

    ctx.html(AdminConfigDataSourcePage::class, vo)
  }

  private fun List<ConfigurationModal>.getByKey(key: String): String? {
    return find { it.configKey == key }?.configValue
  }
}
