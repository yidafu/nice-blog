package dev.yidafu.blog.admin

import dev.yidafu.blog.admin.jobs.SynchronousJob
import dev.yidafu.blog.admin.routes.mountAdminRoutes
import dev.yidafu.blog.common.ConfigurationKeys
import dev.yidafu.blog.common.ConstantKeys
import dev.yidafu.blog.common.routes.mountPublicRoutes
import dev.yidafu.blog.common.services.ConfigurationService
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.LoggerHandler
import io.vertx.kotlin.coroutines.CoroutineRouterSupport
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.coAwait
import kotlinx.coroutines.runBlocking
import org.koin.core.Koin
import org.quartz.CronScheduleBuilder.cronSchedule
import org.quartz.JobBuilder
import org.quartz.TriggerBuilder
import org.quartz.TriggerKey.triggerKey
import org.quartz.impl.StdSchedulerFactory
import org.slf4j.LoggerFactory

class AdminVerticle(private val koin: Koin) : CoroutineVerticle(), CoroutineRouterSupport {
  private val log = LoggerFactory.getLogger(AdminVerticle::class.java)

  private val scheduler = StdSchedulerFactory.getDefaultScheduler()

  override suspend fun start() {
    super.start()

    try {
      val server = vertx.createHttpServer()
      val router = Router.router(vertx)
      router.route().handler(LoggerHandler.create())

      mountAdminRoutes(router, koin, vertx)
      mountPublicRoutes(router)

      router.errorHandler(404) { ctx ->
        ctx.end("<h1>404 Not Found</h1>")
      }
      server
        .requestHandler(router)
        .listen(8081).coAwait()

      startSyncScheduler()
      println("HTTP Admin server started on port 8081")
    } catch (e: Exception) {
      log.error("admin verticle", e)
    }
  }

  /**
   * execute schedule job
   */
  private suspend fun startSyncScheduler() {
    val configService = koin.get<ConfigurationService>()
    val cronExpr = configService.getByKey(ConfigurationKeys.SYNC_CRON_EXPR).configValue

    log.info("create schedule job with $cronExpr")
    val job =
      JobBuilder.newJob(SynchronousJob::class.java)
        .withIdentity(SynchronousJob.NAME, SynchronousJob.GROUP)
        .build()
    val bus = vertx.eventBus()
    bus.consumer<String>(ConstantKeys.UPDATE_CRON_EXPR) { msg ->
      val cronExpMsg = msg.body()
      log.info("update cron expression ($cronExpMsg)")
      val triggerKey = triggerKey(SynchronousJob.TRIGGER, SynchronousJob.GROUP)
      if (scheduler.checkExists(triggerKey)) {
        // stop schedule job
        scheduler.unscheduleJob(triggerKey)
      }
      // start new schedule plan
      val newTrigger =
        TriggerBuilder.newTrigger()
          .withIdentity(SynchronousJob.TRIGGER, SynchronousJob.GROUP)
          .withSchedule(cronSchedule(cronExpMsg))
          .forJob(SynchronousJob.NAME, SynchronousJob.GROUP)
          .build()
      scheduler.scheduleJob(job, newTrigger)
    }
    // trigger update schedule job
    bus.send(ConstantKeys.UPDATE_CRON_EXPR, cronExpr)
  }

  override suspend fun stop() {
    scheduler.shutdown()
    super.stop()
  }
}
