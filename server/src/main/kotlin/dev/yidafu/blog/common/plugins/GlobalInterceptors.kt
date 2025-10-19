package dev.yidafu.blog.common.plugins

import de.comahe.i18n4k.Locale
import dev.yidafu.blog.common.ConfigurationKeys
import dev.yidafu.blog.common.ConstantKeys
import dev.yidafu.blog.common.bean.bo.ConfigurationBO
import dev.yidafu.blog.common.services.ConfigurationService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.di.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.util.*
import org.slf4j.LoggerFactory
import kotlin.time.Duration.Companion.days

/**
 * 全局配置拦截器
 * 从数据库读取配置并设置到 call.attributes
 */
fun Application.configureGlobalConfiguration() {
  intercept(ApplicationCallPipeline.Plugins) {
    val configService = this@configureGlobalConfiguration.dependencies.resolve<ConfigurationService>()
    val configs = configService.getAll()
    val siteTitle = configs.find { it.configKey == ConfigurationKeys.SITE_TITLE }?.configValue ?: ""
    val githubUrl = configs.find { it.configKey == ConfigurationKeys.GITHUB_URL }?.configValue ?: ""

    val bo = ConfigurationBO(siteTitle, githubUrl)
    call.attributes.put(AttributeKey<ConfigurationBO>(ConstantKeys.GLOBAL_CONFIGURATION), bo)

    proceed()
  }
}

/**
 * 语言处理拦截器
 * 处理 ?lang= 参数和 cookie，设置 locale 到 call.attributes
 */
fun Application.configureLocaleHandler() {
  intercept(ApplicationCallPipeline.Plugins) {
    val language = call.request.queryParameters["lang"]

    // 如果 URL 参数包含 lang，设置 cookie 并重定向
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
      val uri = call.request.uri.replace("lang=$language", "").removeSuffix("?").removeSuffix("&")
      call.respondRedirect(uri)
      finish()
      return@intercept
    }

    // 从 cookie 或 Accept-Language header 获取语言
    val cookieLang = call.request.cookies[ConstantKeys.LANGUAGE_COOKIE_KEY]

    val cLang =
      if (cookieLang.isNullOrEmpty()) {
        call.request.header(HttpHeaders.AcceptLanguage)
      } else {
        cookieLang
      }
    val finalLanguage = cLang ?: ConstantKeys.DEFAULT_LANGUAGE
    
    val locale = Locale.forLanguageTag(finalLanguage)
    
    call.attributes.put(
      AttributeKey<Locale>(ConstantKeys.LANGUAGE_CONTEXT),
      locale,
    )

    proceed()
  }
}

