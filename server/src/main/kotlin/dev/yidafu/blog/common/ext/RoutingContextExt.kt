package dev.yidafu.blog.common.ext

import de.comahe.i18n4k.Locale
import dev.yidafu.blog.common.ConstantKeys
import dev.yidafu.blog.common.TemplateManagerLoader
import dev.yidafu.blog.common.bean.bo.ConfigurationBO
import dev.yidafu.blog.common.controller.CommonController
import dev.yidafu.blog.themes.DataModal
import dev.yidafu.blog.themes.NotFoundPage
import dev.yidafu.blog.themes.Page
import dev.yidafu.blog.themes.TemplateManager
import dev.yidafu.blog.themes.simple.SimpleTemplateManager
import io.vertx.core.Future
import io.vertx.core.http.HttpHeaders
import io.vertx.core.impl.ContextInternal
import io.vertx.core.json.EncodeException
import io.vertx.ext.web.RoutingContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.*

internal val jsonCodec =
  Json {
    encodeDefaults = true
    ignoreUnknownKeys = true
  }

internal inline fun <reified T> RoutingContext.kJson(obj: T): Future<Void>? {
  val res = response()
  val hasContentType: Boolean = res.headers().contains(HttpHeaders.CONTENT_TYPE)
  try {
    val str = jsonCodec.encodeToString<T>(obj)

    // http://www.iana.org/assignments/media-types/application/json
    // No "charset" parameter is defined for this registration.
    // Adding one really has no effect on compliant recipients.

    // apply the content type header only if the encoding succeeds and content type header is not set
    if (!hasContentType) {
      res.putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
    }
    return res.end(str)
  } catch (e: EncodeException) {
    // handle the failure
    fail(e)
    // as the operation failed return a failed future
    // this is purely a notification
    return (vertx().getOrCreateContext() as ContextInternal).failedFuture<Void>(e)
  } catch (e: UnsupportedOperationException) {
    fail(e)
    return (vertx().getOrCreateContext() as ContextInternal).failedFuture<Void>(e)
  }
}

internal inline fun <reified V> RoutingContext.render(
  pageName: String,
  vo: V,
) {
  val local = this.get<Locale>(ConstantKeys.LANGUAGE_CONTEXT)

  val configBo = this.get<ConfigurationBO>(CommonController.GLOBAL_CONFIGURATION)

  val voData = Json.encodeToJsonElement(vo)
  val modalObject =
    JsonObject(
      mapOf(
        DataModal.COMMON_LOCALE to local.toString().toJson(),
        DataModal.CURRENT_PATH to request().path().toJson(),
        DataModal.SITE_TITLE to configBo.siteTitle.toJson(),
        DataModal.GITHUB_URL to configBo.githubUrl.toJson(),
        DataModal.VO_DATA to voData,
      ),
    )
  val tplManager = TemplateManagerLoader.getTemplateManager(SimpleTemplateManager.NAME)
  val page = tplManager.createPage(pageName, DataModal(modalObject))

  this.end(page.createPageHtml())
}

fun TemplateManager.createPage(
  pageName: String,
  modal: DataModal,
): Page {
  val page = getPageProvider(pageName)?.createPage(modal)
  return page ?: NotFoundPage()
}

inline fun String.toJson() = JsonPrimitive(this)
