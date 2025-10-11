package dev.yidafu.blog.admin

import dev.yidafu.blog.admin.jobs.SynchronousJob
import dev.yidafu.blog.common.ConfigurationKeys
import dev.yidafu.blog.common.services.ConfigurationService
import io.ktor.http.HttpHeaders
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.callid.CallId
import io.ktor.server.plugins.di.dependencies
import io.ktor.server.plugins.openapi.openAPI
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.routing
import io.ktor.server.sessions.Sessions
import org.quartz.CronScheduleBuilder.cronSchedule
import org.quartz.JobBuilder
import org.quartz.TriggerBuilder
import org.quartz.impl.StdSchedulerFactory
import org.slf4j.LoggerFactory

suspend fun Application.adminModule() {
//  injectAllDependencies()

  val logger = LoggerFactory.getLogger("admin")

  val scheduler = StdSchedulerFactory.getDefaultScheduler()

  install(Sessions)
  install(CallId) {
    header(HttpHeaders.XRequestId)
    verify { callId: String ->
      callId.isNotEmpty()
    }
  }

  install(StatusPages) {
    exception<Throwable> { call, cause ->
      logger.error("Error in admin module", cause)
      call.respondText("Internal Server Error", status = io.ktor.http.HttpStatusCode.InternalServerError)
    }
  }
  routing {
    openAPI(path = "openapi", swaggerFile = "openapi/documentation.yaml")
  }

//  createRoutes2()

  val configService = dependencies.resolve<ConfigurationService>()

  suspend fun startSyncScheduler(
    scheduler: org.quartz.Scheduler,
    configService: ConfigurationService,
  ) {
    val cronExpr = configService.getByKey(ConfigurationKeys.SYNC_CRON_EXPR).configValue

    val logger = LoggerFactory.getLogger("AdminModule")
    logger.info("Creating schedule job with $cronExpr")

    val job =
      JobBuilder.newJob(SynchronousJob::class.java)
        .withIdentity(SynchronousJob.NAME, SynchronousJob.GROUP)
        .build()

    // 在实际应用中，你可能需要一个事件总线来更新调度表达式
    // 这里为了简化，我们直接启动调度器
    val trigger =
      TriggerBuilder.newTrigger()
        .withIdentity(SynchronousJob.TRIGGER, SynchronousJob.GROUP)
        .withSchedule(cronSchedule(cronExpr))
        .forJob(SynchronousJob.NAME, SynchronousJob.GROUP)
        .build()

    scheduler.scheduleJob(job, trigger)
  }

  startSyncScheduler(scheduler, configService)
}
