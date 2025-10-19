package dev.yidafu.blog.common.ext

import de.comahe.i18n4k.Locale
import dev.yidafu.blog.common.ConstantKeys
import dev.yidafu.blog.common.TemplateManagerLoader
import dev.yidafu.blog.common.bean.bo.ConfigurationBO
import dev.yidafu.blog.themes.DataModal
import dev.yidafu.blog.themes.simple.SimpleTemplateManager
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.request.uri
import io.ktor.server.routing.*
import io.ktor.util.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.encodeToJsonElement
import java.util.*

fun Route.any(
  path: String,
  body: RoutingHandler,
): Route {
  return route(path) { handle(body) }
}

suspend inline fun ApplicationCall.render(
  pageName: String,
) {
  render(pageName, Unit)
}

suspend inline fun <reified T> ApplicationCall.render(
  pageName: String,
  voData: T,
) {
  // 获取 locale，如果没有设置则使用默认值
  val locale =
    try {
      attributes.getOrNull(AttributeKey<Locale>(ConstantKeys.LANGUAGE_CONTEXT))
        ?: Locale.forLanguageTag(ConstantKeys.DEFAULT_LANGUAGE)
    } catch (e: Exception) {
      Locale.forLanguageTag(ConstantKeys.DEFAULT_LANGUAGE)
    }

  // 获取全局配置
  val config =
    try {
      attributes.getOrNull(AttributeKey<ConfigurationBO>(ConstantKeys.GLOBAL_CONFIGURATION))
        ?: ConfigurationBO("", "")
    } catch (e: Exception) {
      ConfigurationBO("", "")
    }

  // 序列化 voData 为 JsonElement
  val voDataJson: JsonElement = Json.encodeToJsonElement(voData)

  // 构建 DataModal
  val dataStore =
    JsonObject(
      mapOf(
        DataModal.COMMON_LOCALE to Json.encodeToJsonElement(locale.toLanguageTag()),
        DataModal.CURRENT_PATH to Json.encodeToJsonElement(request.uri),
        DataModal.SITE_TITLE to Json.encodeToJsonElement(config.siteTitle),
        DataModal.GITHUB_URL to Json.encodeToJsonElement(config.githubUrl),
        DataModal.VO_DATA to voDataJson,
      ),
    )

  val modal = DataModal(dataStore)

  // 获取 TemplateManager 和 PageProvider
  val templateManager = TemplateManagerLoader.getTemplateManager(SimpleTemplateManager.NAME)
  val pageProvider =
    templateManager.getPageProvider(pageName)
      ?: throw IllegalArgumentException("PageProvider for '$pageName' not found")

  // 创建 Page
  val page = pageProvider.createPage(modal)

  // 渲染页面
  respondHtml {
    page.render(this)
  }
}
