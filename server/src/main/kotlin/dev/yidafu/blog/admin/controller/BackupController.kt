package dev.yidafu.blog.admin.controller

import dev.yidafu.blog.common.Routes
import dev.yidafu.blog.ksp.annotation.Controller
import dev.yidafu.blog.ksp.annotation.Post
import io.vertx.ext.web.RoutingContext
import org.koin.core.annotation.Single

@Single
@Controller
class BackupController {
  @Post(Routes.ADMIN_BACKUP_URL)
  suspend fun createBackup(ctx: RoutingContext) {
  }
}
