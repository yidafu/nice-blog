package dev.yidafu.blog.admin.controller

import dev.yidafu.blog.common.ConfigurationKeys
import dev.yidafu.blog.common.FormKeys
import dev.yidafu.blog.common.Routes
import dev.yidafu.blog.common.dto.ConfigurationDTO
import dev.yidafu.blog.common.ext.getByKey
import dev.yidafu.blog.common.ext.render
import dev.yidafu.blog.common.services.ConfigurationService
import dev.yidafu.blog.common.vo.AdminAppearanceVO
import dev.yidafu.blog.common.vo.AdminDataSourceVO
import dev.yidafu.blog.common.vo.AdminSynchronousVO
import dev.yidafu.blog.common.annotation.Controller
import dev.yidafu.blog.common.annotation.Get
import dev.yidafu.blog.common.annotation.Post
import dev.yidafu.blog.themes.PageNames
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import org.slf4j.LoggerFactory

@Controller
class ConfigurationController(
  private val configService: ConfigurationService,
) {
  private val log = LoggerFactory.getLogger(ConfigurationController::class.java)

  @Get(Routes.CONFIG_APPEARANCE_URL)
  suspend fun appearancePage(call: ApplicationCall) {
    val vo = AdminAppearanceVO()
    call.render(PageNames.ADMIN_CONFIG_APPEARANCE_PAGE, mapOf("config" to vo))
  }

  /**
   * 通用更新配置
   */
  @Post(Routes.CONFIGURATION_URL)
  suspend fun updateConfigAction(call: ApplicationCall) {
    log.info("updateAppearancePage")
    val body = call.receiveParameters()
    val referer = call.request.header(HttpHeaders.Referrer) ?: Routes.CONFIGURATION_URL

    val dtoList =
      listOf(
        FormKeys.SITE_TITLE to ConfigurationKeys.SITE_TITLE,
        FormKeys.GITHUB_URL to ConfigurationKeys.GITHUB_URL,
        FormKeys.CRON_EXPR to ConfigurationKeys.SYNC_CRON_EXPR,
        FormKeys.SOURCE_TYPE to ConfigurationKeys.SOURCE_TYPE,
        FormKeys.SOURCE_URL to ConfigurationKeys.SOURCE_URL,
        FormKeys.SOURCE_TOKEN to ConfigurationKeys.SOURCE_TOKEN,
        FormKeys.SOURCE_BRANCH to ConfigurationKeys.SOURCE_BRANCH,
        FormKeys.FEISHU_APP_ID to ConfigurationKeys.FEISHU_APP_ID,
        FormKeys.FEISHU_APP_SECRET to ConfigurationKeys.FEISHU_APP_SECRET,
      ).mapNotNull { keyPair ->
        body[keyPair.first]?.let { value ->
          ConfigurationDTO(keyPair.second, value)
        }
      }
    dtoList.find { it.configKey == ConfigurationKeys.SYNC_CRON_EXPR }?.let { config ->
      log.info("send update cron expression event => ${config.configValue}")
//      vertx.eventBus().send(ConstantKeys.UPDATE_CRON_EXPR, config.configValue)
    }

    log.info("start update config ${dtoList.joinToString(",")}}")
    configService.updateConfig(dtoList)
    call.respondRedirect(referer)
  }

  @Get(Routes.CONFIG_SYNC_URL)
  suspend fun synchronousPage(call: ApplicationCall) {
    val config = configService.getByKey(ConfigurationKeys.SYNC_CRON_EXPR)
    val vo =
      AdminSynchronousVO(
        config.configValue,
      )
    call.render(PageNames.ADMIN_CONFIG_SYNC_PAGE, mapOf("config" to vo))
  }

  @Get(Routes.CONFIG_DATA_SOURCE_URL)
  suspend fun dataSourcePage(call: ApplicationCall) {
    val configs =
      configService.getByKeys(
        listOf(
          ConfigurationKeys.SOURCE_TYPE,
          ConfigurationKeys.SOURCE_URL,
          ConfigurationKeys.SOURCE_TOKEN,
          ConfigurationKeys.SOURCE_BRANCH,
          ConfigurationKeys.FEISHU_APP_ID,
          ConfigurationKeys.FEISHU_APP_SECRET,
        ),
      )

    val vo =
      AdminDataSourceVO(
        configs.getByKey(ConfigurationKeys.SOURCE_TYPE) ?: "",
        configs.getByKey(ConfigurationKeys.SOURCE_URL) ?: "",
        configs.getByKey(ConfigurationKeys.SOURCE_TOKEN) ?: "",
        configs.getByKey(ConfigurationKeys.SOURCE_BRANCH) ?: "",
        configs.getByKey(ConfigurationKeys.FEISHU_APP_ID) ?: "",
        configs.getByKey(ConfigurationKeys.FEISHU_APP_SECRET) ?: "",
      )

    call.render(PageNames.ADMIN_CONFIG_DATA_SOURCE_PAGE, mapOf("config" to vo))
  }
}
