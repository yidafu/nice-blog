package dev.yidafu.blog.admin.jobs

import dev.yidafu.blog.common.dao.tables.references.B_SYNC_TASK
import dev.yidafu.blog.common.services.BaseService
import dev.yidafu.blog.dev.yidafu.blog.engine.Logger
import org.jooq.CloseableDSLContext
import org.jooq.impl.DSL.concat
import org.slf4j.LoggerFactory

class DBLogger(
  private val context: CloseableDSLContext,
) : Logger, BaseService(context) {
  private val log = LoggerFactory.getLogger(DBLogger::class.java)

  override suspend fun log(
    taskId: String,
    str: String,
  ) = runDB {
    log.warn("[$taskId] $str")
    context.update(B_SYNC_TASK).set(
      B_SYNC_TASK.LOGS,
      concat(B_SYNC_TASK.LOGS, str + "\n"),
    ).where(B_SYNC_TASK.UUID.eq(taskId)).execute()
    Unit
  }
}
