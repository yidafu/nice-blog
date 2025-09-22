package dev.yidafu.blog.engine

import dev.yidafu.blog.common.db.dao.SyncTaskEntity
import dev.yidafu.blog.common.db.tables.SyncTaskTable
import dev.yidafu.blog.common.modal.SyncTaskStatus
import dev.yidafu.blog.common.services.ExposedBaseService
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toKotlinLocalDateTime
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.v1.core.eq
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class DBSynchronousListener(
  private val config: GitConfig,
  private val logger: Logger,
) : SynchronousListener, ExposedBaseService() {
  override fun onStart() {
    logger.logSync("start synchronous task ==> ${config.uuid}")
    changeStatus(config.uuid, SyncTaskStatus.Running)
  }

  override fun onFinish() {
    logger.logSync("Synchronous Task Finished!")
    changeStatus(config.uuid, SyncTaskStatus.Finished)
  }

  override fun onFailed(e: Exception) {
    logger.logSync("Synchronous Task Failed!")
    logger.logSync(e.stackTraceToString())
    changeStatus(config.uuid, SyncTaskStatus.Failed)
  }

  @OptIn(ExperimentalTime::class)
  private fun changeStatus(
    uuid: String,
    status: SyncTaskStatus,
  ): Boolean =
    runDBBlocking {
      val task = SyncTaskEntity.find { SyncTaskTable.uuid eq uuid }.singleOrNull()
      if (task != null) {
        task.status = status
        task.updatedAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        true
      } else {
        false
      }
    }
}
