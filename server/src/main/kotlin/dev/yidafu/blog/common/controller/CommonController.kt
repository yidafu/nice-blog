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

  @Any
  suspend fun initConfiguration(call: ApplicationCall) {
    val configs = configurationService.getAll()
    val siteTitle = configs.find { it.configKey == ConfigurationKeys.SITE_TITLE }?.configValue ?: ""
    val githubUrl = configs.find { it.configKey == ConfigurationKeys.GITHUB_URL }?.configValue ?: ""

    val bo = ConfigurationBO(siteTitle, githubUrl)
    log.info("config bo $bo")
    call.attributes.put(AttributeKey<ConfigurationBO>(ConstantKeys.GLOBAL_CONFIGURATION), bo)
    // Ktor中不需要显式调用next()，拦截器会自动继续
  }

  @Any
  suspend fun localHandler(call: ApplicationCall) {
    val language = call.request.queryParameters["lang"]

    if (!language.isNullOrBlank()) {
      call.response.cookies.append(
        Cookie(
          name = ConstantKeys.LANGUAGE_COOKIE_KEY,
          value = language,
          path = "/",
          maxAge = 30.days.inWholeSeconds.toInt(),
          httpOnly = true,
        ),
      )
      val uri = call.request.uri.replace("lang=$language", "").removeSuffix("?")
      call.respondRedirect(uri)
      return
    }

    val cookieLang = call.request.cookies[ConstantKeys.LANGUAGE_COOKIE_KEY]

    val cLang =
      if (cookieLang.isNullOrEmpty()) {
        call.request.header(HttpHeaders.AcceptLanguage)
      } else {
        cookieLang
      }
    val finalLanguage = cLang ?: ConstantKeys.DEFAULT_LANGUAGE
    log.info("current language $finalLanguage")
    call.attributes.put(
      AttributeKey(ConstantKeys.LANGUAGE_CONTEXT),
      Locale.forLanguageTag(finalLanguage),
    )
    // Ktor中不需要显式调用next()，拦截器会自动继续
  }

  fun uploadStaticFiles(call: ApplicationCall) {
  }

  companion object {
    const val GLOBAL_CONFIGURATION = "global_configuration"
  }
}
