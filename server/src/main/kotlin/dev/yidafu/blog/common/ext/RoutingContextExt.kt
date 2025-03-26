package dev.yidafu.blog.common.ext

import de.comahe.i18n4k.Locale
import dev.yidafu.blog.common.ConstantKeys
import dev.yidafu.blog.common.bean.bo.ConfigurationBO
import dev.yidafu.blog.common.controller.CommonController
import dev.yidafu.blog.common.view.tpl.PageTemplate
import dev.yidafu.blog.common.vo.BaseVO
import dev.yidafu.blog.common.vo.PageVO
import io.vertx.core.Future
import io.vertx.core.http.HttpHeaders
import io.vertx.core.impl.ContextInternal
import io.vertx.core.json.EncodeException
import io.vertx.ext.web.RoutingContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

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

internal inline fun <V : PageVO, T : PageTemplate<V>> RoutingContext.html(
  pageClass: Class<T>,
  vo: V,
) {
  val local = this.get<Locale>(ConstantKeys.LANGUAGE_CONTEXT)

  val configBo = this.get<ConfigurationBO>(CommonController.GLOBAL_CONFIGURATION)

  val baseVO =
    BaseVO(
      locale = local,
      currentPath = request().path(),
      siteTitle = configBo.siteTitle,
      githubUrl = configBo.githubUrl,
    )
  vo.baseVO = baseVO
  val page: T? = pageClass.constructors.find { it.parameters.size == 1 }?.newInstance(vo) as T?
//  = pageClass.primaryConstructor?.call(vo)

  val htmlStr = page?.render()

  response().putHeader(HttpHeaders.CONTENT_TYPE, "text/html")
  this.end(htmlStr)
}
