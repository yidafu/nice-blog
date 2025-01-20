package dev.yidafu.blog.admin.jobs

import dev.yidafu.blog.admin.services.SyncTaskService
import dev.yidafu.blog.dev.yidafu.blog.engine.Logger
import org.koin.java.KoinJavaComponent.inject

class DBLogger(private val uuid: String) : Logger {
  private val syncTaskService: SyncTaskService by inject(SyncTaskService::class.java)

  override suspend fun log(str: String) {
    syncTaskService.appendLog(uuid, str)
  }
}
