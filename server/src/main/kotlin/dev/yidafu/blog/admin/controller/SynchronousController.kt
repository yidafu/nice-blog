package dev.yidafu.blog.admin.controller

import dev.yidafu.blog.admin.manager.SynchronousManager
import dev.yidafu.blog.admin.services.SyncTaskService
import dev.yidafu.blog.common.Routes
import dev.yidafu.blog.common.converter.SyncTaskConvertor
import dev.yidafu.blog.common.modal.SyncTaskStatus
import dev.yidafu.blog.common.query.PageQuery
import dev.yidafu.blog.common.services.ArticleService
import dev.yidafu.blog.common.services.ConfigurationService
import dev.yidafu.blog.common.vo.AdminSynchronousVO
import dev.yidafu.blog.common.vo.PaginationVO
import dev.yidafu.blog.common.annotation.Controller
import dev.yidafu.blog.common.annotation.Get
import dev.yidafu.blog.themes.PageNames
import io.ktor.server.application.*
import io.ktor.server.response.*
import kotlinx.coroutines.delay
import kotlinx.html.div
import kotlinx.html.stream.appendHTML
import kotlinx.html.style
import org.mapstruct.factory.Mappers
import org.slf4j.LoggerFactory

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

  @Get(Routes.SYNC_URL)
  suspend fun syncPage(call: ApplicationCall) {
    call.respondRedirect(Routes.SYNC_OPERATE_URL)
  }

  @Get(Routes.SYNC_LOG_URL)
  suspend fun syncLogListPage(call: ApplicationCall) {
    val pageNum = call.request.queryParameters["page"]?.toInt() ?: 1
    val pageSize = call.request.queryParameters["size"]?.toInt() ?: 10

    val (total, list) = syncTaskService.getSyncLogs(PageQuery(pageNum, pageSize))

    val vo = PaginationVO(pageNum, pageSize, total, syncTaskConvertor.toVOList(list))
    call.respondText("Rendering page: ${PageNames.ADMIN_CONFIG_SYNC_LOG_LIST_PAGE} with data: $vo")
  }

  @Get(Routes.SYNC_LOG_DETAIL_URL)
  suspend fun syncLogDetailPage(call: ApplicationCall) {
    val uuid = call.request.queryParameters["uuid"] ?: return
    val log = syncTaskService.getSyncLog(uuid)

    val vo = syncTaskConvertor.toDTO(log)
    call.respondText("Rendering page: ${PageNames.ADMIN_CONFIG_SYNC_LOG_DETAIL_PAGE} with data: $vo")
  }

  @Get(Routes.SYNC_OPERATE_URL)
  suspend fun syncOperatePage(call: ApplicationCall) {
    call.respondText("Rendering page: ${PageNames.ADMIN_CONFIG_SYNC_LOG_OPERATE_PAGE} with data: ${AdminSynchronousVO("")}")
  }

  @Get(Routes.SYNC_API_START_URL)
  suspend fun startSync(call: ApplicationCall) {
    val forceSync = call.request.queryParameters["force"] == "1"
    val taskUuid = synchronousManager.startSync(forceSync)

    log.info("start synchronous task {}", taskUuid)
    val htmlFragment =
      buildString {
        appendHTML().div {
          style = "background-color: #f9fafb; padding: 0.25rem; border-radius: 0.5rem; border: 1px solid #d1d5db; display: inline-block;"
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
    call.respondText(htmlFragment)
  }

  /**
   * https://github.com/auryn31/sse-vertx
   */
  @Get(Routes.SYNC_API_LOG_URL)
  suspend fun getSyncLog(call: ApplicationCall) {
    val uuid = call.parameters["uuid"] ?: return
    call.response.headers.append("Content-Type", "text/event-stream;charset=UTF-8")
    call.response.headers.append("Connection", "keep-alive")
    call.response.headers.append("Cache-Control", "no-cache")
    var previewLog = ""

    // max connection time 10 minutes
    repeat(60 * 10) {
      val log = syncTaskService.getSyncLog(uuid)

      if (log.id != null) {
        if (log.status == SyncTaskStatus.Finished || log.status == SyncTaskStatus.Failed) {
          (log.logs ?: "").split('\n').forEach { str ->
//            call.respondTextWriter { writer ->
//
//              writer.write(
//                SseModel(
//                  data = str,
//                  event = logAppendEvent,
//                ).toString()
//              )
//              writer.flush()
//            }
          }
//          call.respondTextWriter().use { writer ->
//            writer.write(
//              SseModel(
//                data = "=== LOG END ===",
//                event = logEndEvent,
//              ).toString()
//            )
//            writer.flush()
//          }
          return
        } else {
          val currentLog = log.logs
          if (currentLog != null) {
            val appendText = if (previewLog.isEmpty()) currentLog else currentLog.substring(previewLog.length)
            previewLog = currentLog
            if (appendText.isNotEmpty()) {
              appendText.split('\n').forEach { str ->
//                call.respondTextWriter().use { writer ->
//                  writer.write(
//                    SseModel(
//                      data = str,
//                      event = logAppendEvent,
//                    ).toString()
//                  )
//                  writer.flush()
//                }
              }
            }
          }
        }
      }
      delay(1000)
    }
  }
}
