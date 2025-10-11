package dev.yidafu.blog.fe.controller

import dev.yidafu.blog.common.ConstantKeys
import dev.yidafu.blog.common.Routes
import dev.yidafu.blog.common.ShortUUID
import dev.yidafu.blog.common.converter.ArticleConvertor
import dev.yidafu.blog.common.dto.AccessLogDTO
import dev.yidafu.blog.common.ext.now
import dev.yidafu.blog.common.services.ArticleService
import dev.yidafu.blog.fe.service.AccessLogService
import dev.yidafu.blog.common.annotation.Any
import dev.yidafu.blog.common.annotation.Controller
import dev.yidafu.blog.common.annotation.Get
import dev.yidafu.blog.themes.PageNames
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.ApplicationRequest
import io.ktor.server.request.header
import io.ktor.server.request.uri
import io.ktor.server.response.*
import kotlinx.datetime.LocalDateTime
import org.mapstruct.factory.Mappers
import org.slf4j.LoggerFactory
import kotlin.time.Duration.Companion.days

@Controller
class HomeController(
  private val articleService: ArticleService,
  private val accessLogService: AccessLogService,
) {
  private val log = LoggerFactory.getLogger(HomeController::class.java)
  private val articleConvertor = Mappers.getMapper(ArticleConvertor::class.java)

//  @Any("/*")
  fun anyAccessLog(call: ApplicationCall) {
    val url = call.request.uri
    val referer = call.request.header(HttpHeaders.Referrer) ?: ""
    val ua = call.request.header(HttpHeaders.UserAgent)
    val ip = getClientIp(call.request)
    val cookie = call.request.cookies[ConstantKeys.COOKIE_UID_KEY]
    val uid =
      cookie ?: run {
        val uid = ShortUUID.generate()
        call.response.cookies.append(
          Cookie(
            name = ConstantKeys.COOKIE_UID_KEY,
            value = uid,
            path = "/",
            maxAge = 365.days.inWholeSeconds.toInt(),
            httpOnly = true,
//            sameSite = CookieSameSite.Strict
          ),
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
        ua = ua ?: "",
      )
    accessLogService.saveLog(dto)
    // Ktor中不需要显式调用next()，拦截器会自动继续
  }

  @Get(Routes.ROOT_URL)
  @Get(Routes.ARTICLE_LIST)
  suspend fun indexPage(call: ApplicationCall) {
    try {
      val page = call.parameters["page"]?.toInt() ?: 1
    val list = articleService.getAll()
    val voList = articleConvertor.toVO(list)

    // 在Ktor中渲染页面
    call.respondText("Rendering page: ${PageNames.ARTICLE_LIST} with data: $voList")
    } catch (e: Exception) {
      log.error("查询文章列表失败", e)
      call.respondRedirect("/403")
    }
  }

  @Get(Routes.ARTICLE_DETAIL)
  suspend fun articlePage(call: ApplicationCall) {
    val id = call.parameters["identifier"] ?: ""
    val article = articleService.getOneByIdentifier(id)
    article?.let {
      val vo = articleConvertor.toVO(it)
      call.respondText("Rendering page: ${PageNames.ARTICLE_DETAIL} with data: $vo")
    } ?: run {
      call.respondRedirect("/404")
    }
  }

  private fun getClientIp(request: ApplicationRequest): String {
    val forwardIp = request.header("X-Forwarded-For")
    if (forwardIp?.isNotEmpty() == true) {
      val ip = forwardIp.split(",").firstOrNull()
      if (ip?.isNotBlank() == true) {
        return ip
      }
    }

    val realIp: String? = request.header("X-Real-IP")
    if (realIp?.isNotBlank() == true) {
      return realIp
    }
    return request.local.remoteHost
  }
}
