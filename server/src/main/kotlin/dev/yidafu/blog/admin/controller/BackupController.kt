package dev.yidafu.blog.admin.controller

import dev.yidafu.blog.admin.services.BackupService
import dev.yidafu.blog.common.Routes
import dev.yidafu.blog.common.annotation.Controller
import dev.yidafu.blog.common.annotation.Get
import dev.yidafu.blog.common.annotation.Post
import io.ktor.server.application.*
import io.ktor.server.response.*

@Controller
class BackupController(
  val backupService: BackupService,
) {
  @Get(Routes.ADMIN_BACKUP_URL)
  fun backupPage(call: ApplicationCall) {
  }

  @Post(Routes.ADMIN_BACKUP_URL)
  suspend fun createBackupAction(call: ApplicationCall) {
    backupService.createBackup()
    call.respond(mapOf("success" to true))
  }
}
