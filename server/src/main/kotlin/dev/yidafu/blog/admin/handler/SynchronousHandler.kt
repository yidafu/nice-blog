package dev.yidafu.blog.admin.handler

import dev.yidafu.blog.admin.jobs.NiceGitSynchronousTask
import dev.yidafu.blog.admin.services.SyncTaskService
import dev.yidafu.blog.admin.views.pages.sync.AdminSyncLogPage
import dev.yidafu.blog.admin.views.pages.sync.AdminSyncOperatePage
import dev.yidafu.blog.common.BlogConfig
import dev.yidafu.blog.common.Routes
import dev.yidafu.blog.common.dto.MarkdownArticleDTO
import dev.yidafu.blog.common.ext.html
import dev.yidafu.blog.common.modal.ArticleModel
import dev.yidafu.blog.common.modal.ArticleStatus
import dev.yidafu.blog.common.modal.SyncTaskStatus
import dev.yidafu.blog.common.services.ArticleService
import dev.yidafu.blog.common.vo.AdminSynchronousVO
import dev.yidafu.blog.dev.yidafu.blog.engine.SyncContext
import io.github.allangomes.kotlinwind.css.I300
import io.github.allangomes.kotlinwind.css.I50
import io.github.allangomes.kotlinwind.css.LG
import io.github.allangomes.kotlinwind.css.kw
import io.vertx.ext.web.RoutingContext
import io.vertx.kotlin.coroutines.vertxFuture
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.html.div
import kotlinx.html.pre
import kotlinx.html.stream.appendHTML
import kotlinx.html.style
import org.koin.core.annotation.Single
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Single
class SynchronousHandler(
  private val syncTaskService: SyncTaskService,
  private val articleService: ArticleService,
) {
  private val LOG_APPEND_EVENT = "logAppend"
  private val LOG_END_EVENT = "logEnd"
  suspend fun syncPage(ctx: RoutingContext) {
    ctx.redirect(Routes.SYNC_OPERATE_URL)
  }

  suspend fun syncLogPage(ctx: RoutingContext) {
    ctx.html(AdminSyncLogPage::class, AdminSynchronousVO(""))
  }

  suspend fun syncOperatePage(ctx: RoutingContext) {
    ctx.html(AdminSyncOperatePage::class, AdminSynchronousVO(""))
  }

  @OptIn(ExperimentalUuidApi::class)
  suspend fun startSync(ctx: RoutingContext) {
    val taskUuid = Uuid.random().toHexString()

    syncTaskService.createSyncTask(taskUuid)
    val syncCtx = DbSyncContext(
      taskUuid,
      syncTaskService,
      SyncContext.GitConfig("https://github.com/yidafu/example-blog.git", branch = "master")
    )
    val htmlFragment = buildString {
      appendHTML().div {
        style = kw.inline { background.gray[I50]; padding[4]; border.rounded[LG]; border.gray[I300] }
        attributes["hx-ext"] = "sse"
        attributes["sse-connect"] = Routes.SYNC_API_LOG_URL.replace(":uuid", taskUuid)
        attributes["sse-swap"] = "message"
        attributes["sse-close"] = LOG_END_EVENT

        div {
          attributes["sse-swap"] = LOG_APPEND_EVENT
          attributes["hx-swap"] = "beforeend"
        }
        div {
          attributes["sse-swap"] = LOG_END_EVENT
        }
      }
    }
    ctx.end(htmlFragment)
    // async execute task
    withContext(Dispatchers.IO) {
      NiceGitSynchronousTask(syncCtx, DbDelegate(syncCtx, articleService)).sync()
    }
  }

  /**
   * https://github.com/auryn31/sse-vertx
   */
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
      if (log != null) {
        if (log.status != SyncTaskStatus.Running) {
          (log.logs ?: "").split('\n').forEach { str ->
            response.write(
              SseModel(
                data = str,
                event = LOG_APPEND_EVENT,
              ).toString()
            )
          }
          response.end(
            SseModel(
              data = "=== LOG END ===",
              event = LOG_END_EVENT,
            ).toString()
          )
          return
        } else {
          val currentLog = log.logs
          if (currentLog != null) {
            val appendText = if (previewLog.isEmpty()) currentLog else currentLog.substring(previewLog.length)
            println("${previewLog.isEmpty()}   current log ${currentLog.length},  preview log ${previewLog.length},  appendText ${appendText.length}")
            previewLog = currentLog
            if (appendText.isNotEmpty()) {
              appendText.split('\n').forEach { str ->
                response.write(
                  SseModel(
                    data = str,
                    event = LOG_APPEND_EVENT,
//                      id = pollingCount.toString()
                  ).toString()
                )
              }
            }
          }
        }
      }
      delay(1000)
    }
//    ctx.end("log $uuid")
  }

  class DbDelegate(
    private val ctx: SyncContext,
    private val articleService: ArticleService,
  ) : NiceGitSynchronousTask.DatabaseDelegate {
    override fun uploadFile(file: File): String {
      val directory = File(BlogConfig.DEFAULT_UPLOAD_DIRECTORY)

      if (!directory.exists()) {
        directory.mkdirs()
      }
      val newFilename = UUID.randomUUID().toString() + "." + file.extension
      val newFilePath = Paths.get(directory.path, newFilename)
      ctx.log("copy ${file.toPath()} to $newFilePath")
      Files.copy(file.toPath(), newFilePath)
      return Routes.UPLOAD_URL.replace("*", newFilename)
    }

    override fun updateArticle(dto: MarkdownArticleDTO): Boolean {
      println(dto.filename)
      val identifier = dto.filename.replace(".md", "")
      val modal = ArticleModel(
        title = dto.frontMatter?.title ?: "",
        summary = dto.frontMatter?.description,
        cover = dto.frontMatter?.cover,
        identifier = identifier,
        html = dto.html,
        series = "",
        content = dto.rawContext,
        status = ArticleStatus.Candidate,
      )
      modal.createdAt = dto.createTime
      modal.updatedAt = dto.updateTime
      CoroutineScope(Dispatchers.IO).launch {
        articleService.saveArticle(modal)
      }
      return true
    }

  }

  data class SseModel(
    val event: String? = null,
    val data: String = "",
    val id: String? = null,
    val retry: Number? = null,
  ) {

    override fun toString(): String {
      val sseStrings = arrayListOf<String>()

      if (event != null) sseStrings.add("event: $event")
      sseStrings.add("data: <p>$data</p>")
      if (id != null) sseStrings.add("id: $id")
      if (retry != null) sseStrings.add("retry: $retry")
      sseStrings.add("\n")

      return sseStrings.joinToString(separator = "\n")
    }
  }

  class DbSyncContext(
    private val uuid: String,
    private val syncTaskService: SyncTaskService,
    override val gitConfig: GitConfig,
  ) : SyncContext() {
    private val flow = MutableSharedFlow<String>(10, 10)

    init {
      CoroutineScope(Dispatchers.IO).launch {
        // append log in order
        flow.collect {
          syncTaskService.appendLog(uuid, it)
        }
      }
    }

    override fun log(str: String) {
      flow.tryEmit(str + "\n")
    }

    override fun onStart() {
      CoroutineScope(Dispatchers.IO).launch {
        syncTaskService.changeStatus(uuid, SyncTaskStatus.Running)
      }
    }

    override fun onFinish() {
      CoroutineScope(Dispatchers.IO).launch {
        syncTaskService.changeStatus(uuid, SyncTaskStatus.Finished)
      }
    }

    override fun onFailed() {
      CoroutineScope(Dispatchers.IO).launch {
        syncTaskService.changeStatus(uuid, SyncTaskStatus.Failed)
      }
    }
  }
}
