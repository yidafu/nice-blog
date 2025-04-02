package dev.yidafu.blog.admin.controller

import dev.yidafu.blog.admin.services.BackupService
import dev.yidafu.blog.common.Routes
import dev.yidafu.blog.ksp.annotation.Controller
import dev.yidafu.blog.ksp.annotation.Get
import dev.yidafu.blog.ksp.annotation.Post
import io.vertx.ext.web.RoutingContext
import org.koin.core.annotation.Single

@Single
@Controller
class BackupController(
  val backupService: BackupService,
) {
  @Get(Routes.ADMIN_BACKUP_URL)
  fun backupPage(ctx: RoutingContext) {
  }

  @Post(Routes.ADMIN_BACKUP_URL)
  suspend fun createBackupAction(ctx: RoutingContext) {
    backupService.createBackup()
    ctx.json(true)
  }
}
