package dev.yidafu.blog.common.controller

import dev.yidafu.blog.common.ConfigurationKeys
import dev.yidafu.blog.common.ConstantKeys
import dev.yidafu.blog.common.bean.bo.ConfigurationBO
import dev.yidafu.blog.common.services.ConfigurationService
import dev.yidafu.blog.ksp.annotation.Any
import dev.yidafu.blog.ksp.annotation.Controller
import io.vertx.core.http.Cookie
import io.vertx.core.http.HttpHeaders
import io.vertx.ext.web.RoutingContext
import org.koin.core.annotation.Single
import org.slf4j.LoggerFactory
import java.util.*

@Controller
@Single
class CommonController(
  private val configurationService: ConfigurationService,
) {
  private val log = LoggerFactory.getLogger(CommonController::class.java)

  @Any
  suspend fun initConfiguration(ctx: RoutingContext) {
    val configs = configurationService.getAll()
    val siteTitle = configs.find { it.configKey == ConfigurationKeys.SITE_TITLE }?.configValue ?: ""
    val githubUrl = configs.find { it.configKey == ConfigurationKeys.GITHUB_URL }?.configValue ?: ""

    val bo = ConfigurationBO(siteTitle, githubUrl)
    log.info("config bo $bo")
    ctx.put(ConstantKeys.GLOBAL_CONFIGURATION, bo)
    ctx.next()
  }

  @Any
  fun localHandler(ctx: RoutingContext) {
    val language = ctx.request().getParam("lang")

    if (!language.isNullOrBlank()) {
      ctx.response().addCookie(Cookie.cookie(ConstantKeys.LANGUAGE_COOKIE_KEY, language).setHttpOnly(true))
      val uri = ctx.request().uri().replace("lang=$language", "").removeSuffix("?")
      ctx.redirect(uri)
      return
    }

    val cookieLang = ctx.request().cookies(ConstantKeys.LANGUAGE_COOKIE_KEY)

    val cLang =
      if (cookieLang.isEmpty()) {
        ctx.request().getHeader(HttpHeaders.ACCEPT_LANGUAGE)
      } else {
        cookieLang.first().value
      }
    val finalLanguage = cLang ?: ConstantKeys.DEFAULT_LANGUAGE
    log.info("current language $finalLanguage")
    ctx.put(ConstantKeys.LANGUAGE_CONTEXT, Locale.forLanguageTag(finalLanguage))
    ctx.next()
  }

  fun uploadStaticFiles(ctx: RoutingContext) {
  }

  companion object {
    const val GLOBAL_CONFIGURATION = "global_configuration"
  }
}
