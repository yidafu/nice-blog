package dev.yidafu.blog.admin

import dev.yidafu.blog.admin.jobs.SynchronousJob
import dev.yidafu.blog.common.ConfigurationKeys
import dev.yidafu.blog.common.ConstantKeys.AUTH_CURRENT_USERNAME
import dev.yidafu.blog.common.Routes
import dev.yidafu.blog.common.TemplateManagerLoader
import dev.yidafu.blog.common.db.ExposedDatabase
import dev.yidafu.blog.common.ext.AdminSession
import dev.yidafu.blog.common.plugins.configureGlobalConfiguration
import dev.yidafu.blog.common.plugins.configureLocaleHandler
import dev.yidafu.blog.common.plugins.configureStatusPages
import dev.yidafu.blog.common.routes.mountErrorRoutes
import dev.yidafu.blog.common.routes.mountPublicRoutes
import dev.yidafu.blog.common.services.ConfigurationService
import dev.yidafu.blog.generated.admin.controller.createRoutes1
import dev.yidafu.blog.generated.common.controller.createRoutes2
import dev.yidafu.blog.generated.injectAllDependencies
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.plugins.callid.CallId
import io.ktor.server.plugins.calllogging.CallLogging
import io.ktor.server.plugins.compression.Compression
import io.ktor.server.plugins.compression.deflate
import io.ktor.server.plugins.compression.gzip
import io.ktor.server.plugins.di.dependencies
//import io.ktor.server.plugins.openapi.openAPI
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.sessions.*
import org.quartz.CronScheduleBuilder.cronSchedule
import org.quartz.JobBuilder
import org.quartz.TriggerBuilder
import org.quartz.impl.StdSchedulerFactory
import org.slf4j.LoggerFactory

suspend fun Application.adminModule() {
  // 依赖注入（每个 Application 实例需要独立注入）
  injectAllDependencies()

  // 注意：数据库、模板已在 Application.kt 中统一初始化
  val logger = LoggerFactory.getLogger("admin")

  val scheduler = StdSchedulerFactory.getDefaultScheduler()

  // 安装全局拦截器
  configureGlobalConfiguration()
  configureLocaleHandler()

  // 挂载静态资源路由
  mountPublicRoutes()

  // 挂载错误页面路由
  mountErrorRoutes()

  // 配置统一的错误页面处理
  configureStatusPages()

  install(Sessions) {
    cookie<AdminSession>("ADMIN_SESSION", SessionStorageMemory()) {
      cookie.path = "/"
      cookie.maxAgeInSeconds = 86400 * 7 // 7 days
      cookie.httpOnly = false  // 改为 false，允许 JavaScript 访问（如果需要）
      // 关键：设置 SameSite 策略，Lax 允许同站点的 GET 请求携带 cookie
      cookie.extensions["SameSite"] = "Lax"
    }
  }

  install(CallLogging)

  install(CallId) {
    header(HttpHeaders.XRequestId)
    verify { callId: String ->
      callId.isNotEmpty()
    }
  }

  // 配置压缩
  install(Compression) {
    gzip { }
    deflate { }
  }

  // 为所有 /admin/* 路由添加认证拦截器（登录/登出页面除外）
  intercept(ApplicationCallPipeline.Plugins) {
    val uri = call.request.uri

    if (uri.startsWith(Routes.ADMIN_URL) &&
        !uri.startsWith(Routes.LOGIN_URL) &&
        !uri.startsWith(Routes.LOGOUT_URL)) {
      val username = call.sessions.get<AdminSession>()?.username

      if (username.isNullOrBlank()) {
        call.respondRedirect(Routes.LOGIN_URL)
        finish()
        return@intercept
      }
    }
    proceed()
  }

  // 创建所有路由
  createRoutes1()  // admin 路由

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

//  startSyncScheduler(scheduler, configService)
}
