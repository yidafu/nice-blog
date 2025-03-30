package dev.yidafu.blog.admin.controller

import dev.yidafu.blog.admin.manager.SynchronousManager
import dev.yidafu.blog.admin.services.SyncTaskService
import dev.yidafu.blog.admin.views.pages.sync.AdminSyncLogListPage
import dev.yidafu.blog.admin.views.pages.sync.AdminSyncLogPage
import dev.yidafu.blog.admin.views.pages.sync.AdminSyncOperatePage
import dev.yidafu.blog.common.Routes
import dev.yidafu.blog.common.converter.SyncTaskConvertor
import dev.yidafu.blog.common.ext.html
import dev.yidafu.blog.common.modal.SyncTaskStatus
import dev.yidafu.blog.common.query.PageQuery
import dev.yidafu.blog.common.services.ArticleService
import dev.yidafu.blog.common.services.ConfigurationService
import dev.yidafu.blog.common.sse.SseModel
import dev.yidafu.blog.common.vo.AdminSyncTaskListVO
import dev.yidafu.blog.common.vo.AdminSyncTaskVO
import dev.yidafu.blog.common.vo.AdminSynchronousVO
import dev.yidafu.blog.ksp.annotation.Controller
import dev.yidafu.blog.ksp.annotation.Get
import io.github.allangomes.kotlinwind.css.I300
import io.github.allangomes.kotlinwind.css.I50
import io.github.allangomes.kotlinwind.css.LG
import io.github.allangomes.kotlinwind.css.kw
import io.vertx.ext.web.RoutingContext
import kotlinx.coroutines.delay
import kotlinx.html.div
import kotlinx.html.stream.appendHTML
import kotlinx.html.style
import org.koin.core.annotation.Single
import org.koin.java.KoinJavaComponent.getKoin
import org.mapstruct.factory.Mappers
import org.slf4j.LoggerFactory

@Single
@Controller
class SynchronousController(
  private val syncTaskService: SyncTaskService,
  private val articleService: ArticleService,
  private val configService: ConfigurationService,
  private val synchronousManager: SynchronousManager,
) {
  private val log = LoggerFactory.getLogger(SynchronousController::class.java)
  private val logAppendEvent = "logAppend"
  private val logEndEvent = "logEnd"

  private val syncTaskConvertor = Mappers.getMapper(SyncTaskConvertor::class.java)
  private val koin = getKoin()

  @Get(Routes.SYNC_URL)
  suspend fun syncPage(ctx: RoutingContext) {
    ctx.redirect(Routes.SYNC_OPERATE_URL)
  }

  @Get(Routes.SYNC_LOG_URL)
  suspend fun syncLogListPage(ctx: RoutingContext) {
    val pageNum = ctx.queryParam("page").ifEmpty { listOf("1") }[0].toInt()
    val pageSize = ctx.queryParam("size").ifEmpty { listOf("10") }[0].toInt()

    val (total, list) = syncTaskService.getSyncLogs(PageQuery(pageNum, pageSize))

    val vo = AdminSyncTaskListVO(pageNum, pageSize, total, syncTaskConvertor.toVOList(list))
    ctx.html(AdminSyncLogListPage::class.java, vo)
  }

  @Get(Routes.SYNC_LOG_DETAIL_URL)
  suspend fun syncLogDetailPage(ctx: RoutingContext) {
    val uuid = ctx.queryParam("uuid")[0]
    val log = syncTaskService.getSyncLog(uuid)

    val vo = AdminSyncTaskVO(syncTaskConvertor.toVO(log))
    ctx.html(AdminSyncLogPage::class.java, vo)
  }

  @Get(Routes.SYNC_OPERATE_URL)
  suspend fun syncOperatePage(ctx: RoutingContext) {
    ctx.html(AdminSyncOperatePage::class.java, AdminSynchronousVO(""))
  }

  @Get(Routes.SYNC_API_START_URL)
  suspend fun startSync(ctx: RoutingContext) {
    val taskUuid = synchronousManager.startSync()

    log.info("start synchronous task {}", taskUuid)
    val htmlFragment =
      buildString {
        appendHTML().div {
          style =
            kw.inline {
              background.gray[I50]
              padding[4]
              border.rounded[LG]
              border.gray[I300]
            }
          attributes["hx-ext"] = "sse"
          attributes["sse-connect"] = Routes.SYNC_API_LOG_URL.replace(":uuid", taskUuid)
          attributes["sse-swap"] = "message"
          attributes["sse-close"] = logEndEvent

          div {
            attributes["sse-swap"] = logAppendEvent
            attributes["hx-swap"] = "beforeend"
          }
          div {
            attributes["sse-swap"] = logEndEvent
          }
        }
      }
    ctx.end(htmlFragment)
  }

  /**
   * https://github.com/auryn31/sse-vertx
   */
  @Get(Routes.SYNC_API_LOG_URL)
  suspend fun getSyncLog(ctx: RoutingContext) {
    val uuid = ctx.pathParam("uuid")
    val response = ctx.response()
    response.setChunked(true)

    // set headers
    response.headers().add("Content-Type", "text/event-stream;charset=UTF-8")
    response.headers().add("Connection", "keep-alive")
    response.headers().add("Cache-Control", "no-cache")
//    response.headers().add("Access-Control-Allow-Origin", "*")
    var previewLog = ""

    // max connection time 10 minutes
    repeat(60 * 10) {
      val log = syncTaskService.getSyncLog(uuid)

      if (log.id != null && !response.ended()) {
        if (log.status == SyncTaskStatus.Finished || log.status == SyncTaskStatus.Failed) {
          (log.logs ?: "").split('\n').forEach { str ->
            response.write(
              SseModel(
                data = str,
                event = logAppendEvent,
              ).toString(),
            )
          }
          response.end(
            SseModel(
              data = "=== LOG END ===",
              event = logEndEvent,
            ).toString(),
          )
          return
        } else {
          val currentLog = log.logs
          if (currentLog != null) {
            val appendText = if (previewLog.isEmpty()) currentLog else currentLog.substring(previewLog.length)
            previewLog = currentLog
            if (appendText.isNotEmpty()) {
              appendText.split('\n').forEach { str ->
                response.write(
                  SseModel(
                    data = str,
                    event = logAppendEvent,
//                      id = pollingCount.toString()
                  ).toString(),
                )
              }
            }
          }
        }
      }
      delay(1000)
    }
  }
}
