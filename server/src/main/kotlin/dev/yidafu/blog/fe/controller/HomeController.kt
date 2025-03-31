package dev.yidafu.blog.fe.controller

import dev.yidafu.blog.common.ConstantKeys
import dev.yidafu.blog.common.Routes
import dev.yidafu.blog.common.ShortUUID
import dev.yidafu.blog.common.converter.ArticleConvertor
import dev.yidafu.blog.common.dto.AccessLogDTO
import dev.yidafu.blog.common.ext.render
import dev.yidafu.blog.common.services.ArticleService
import dev.yidafu.blog.fe.service.AccessLogService
import dev.yidafu.blog.ksp.annotation.Any
import dev.yidafu.blog.ksp.annotation.Controller
import dev.yidafu.blog.ksp.annotation.Get
import dev.yidafu.blog.themes.PageNames
import io.vertx.core.http.*
import io.vertx.ext.web.RoutingContext
import org.koin.core.annotation.Single
import org.mapstruct.factory.Mappers
import org.slf4j.LoggerFactory
import java.time.LocalDateTime
import kotlin.time.Duration.Companion.days

@Controller
@Single
class HomeController(
  private val articleService: ArticleService,
  private val accessLogService: AccessLogService,
) {
  private val log = LoggerFactory.getLogger(HomeController::class.java)
  private val articleConvertor = Mappers.getMapper(ArticleConvertor::class.java)

  @Any("/*")
  fun anyAccessLog(ctx: RoutingContext) {
    val url = ctx.request().uri()
    val header = ctx.request().headers()
    val referer = header.get(HttpHeaders.REFERER)
    val ua = header.get(HttpHeaders.USER_AGENT)
    val ip = getClientIp(ctx.request())
    val cookie: Cookie? = ctx.request().getCookie(ConstantKeys.COOKIE_UID_KEY)
    val uid =
      cookie?.value ?: run {
        val uid = ShortUUID.generate()
        ctx.response().addCookie(
          Cookie.cookie(ConstantKeys.COOKIE_UID_KEY, uid).apply {
            sameSite = CookieSameSite.STRICT
            path = "/"
            maxAge = 365.days.inWholeSeconds
          },
        )
        uid
      }
    val dto =
      AccessLogDTO(
        uid = uid,
        accessTime = LocalDateTime.now(),
        sourceUrl = url,
        referrerUrl = referer,
        ip = ip,
        ua = ua,
      )
    accessLogService.saveLog(dto)
    ctx.next()
  }

  @Get(Routes.ROOT_URL)
  @Get(Routes.ARTICLE_LIST)
  suspend fun indexPage(ctx: RoutingContext) {
    val list = articleService.getAll()
    val voList = articleConvertor.toVO(list)

    ctx.render(PageNames.ARTICLE_LIST, voList)
  }

  @Get(Routes.ARTICLE_DETAIL)
  suspend fun articlePage(ctx: RoutingContext) {
    val id = ctx.pathParam("identifier")
    val article = articleService.getOneByIdentifier(id)
    article?.let {
      val vo = articleConvertor.toVO(it)
      ctx.render(PageNames.ARTICLE_DETAIL, vo)
    } ?: run {
      ctx.redirect("/404")
    }
  }

  private fun getClientIp(request: HttpServerRequest): String {
    val header = request.headers()
    val forwardIp = header.get("X-Forwarded-For")
    if (forwardIp?.isNotEmpty() == true) {
      val ip = forwardIp.split(",").firstOrNull()
      if (ip?.isNotBlank() == true) {
        return ip
      }
    }

    val realIp: String? = header.get("X-Real-IP")
    if (realIp?.isNotBlank() == true) {
      return realIp
    }
    return request.remoteAddress().host()
  }
}
