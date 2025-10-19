package dev.yidafu.blog.common.controller

import dev.yidafu.blog.common.ConfigurationKeys
import dev.yidafu.blog.common.ConstantKeys
import dev.yidafu.blog.common.bean.bo.ConfigurationBO
import dev.yidafu.blog.common.services.ConfigurationService
import dev.yidafu.blog.common.annotation.Any
import dev.yidafu.blog.common.annotation.Controller
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.header
import io.ktor.server.request.uri
import io.ktor.server.response.*
import io.ktor.util.AttributeKey
import org.slf4j.LoggerFactory
import java.util.*
import kotlin.time.Duration.Companion.days

@Controller
class CommonController(
  private val configurationService: ConfigurationService,
) {
  private val log = LoggerFactory.getLogger(CommonController::class.java)

  // initConfiguration 和 localHandler 已移至 GlobalInterceptors.kt

  fun uploadStaticFiles(call: ApplicationCall) {
  }

  companion object {
    const val GLOBAL_CONFIGURATION = "global_configuration"
  }
}
