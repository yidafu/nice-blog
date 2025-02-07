package dev.yidafu.blog.engine

import dev.yidafu.blog.common.dao.tables.references.B_SYNC_TASK
import dev.yidafu.blog.common.modal.SyncTaskStatus
import dev.yidafu.blog.engine.Logger
import dev.yidafu.blog.engine.SynchronousListener
import dev.yidafu.blog.engine.config.GitConfig
import org.jooq.CloseableDSLContext

class DBSynchronousListener(
  private val context: CloseableDSLContext,
  private val config: GitConfig,
  private val logger: Logger,
) : SynchronousListener {
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

  private fun changeStatus(
    uuid: String,
    status: SyncTaskStatus,
  ): Boolean {
    context.update(B_SYNC_TASK).set(
      B_SYNC_TASK.STATUS,
      status.ordinal,
    ).where(B_SYNC_TASK.UUID.eq(uuid)).execute()
    return true
  }
}
