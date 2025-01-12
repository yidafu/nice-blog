package dev.yidafu.blog.admin.services

import dev.yidafu.blog.common.Routes
import dev.yidafu.blog.common.converter.SyncTaskConvertor
import dev.yidafu.blog.common.dao.tables.references.B_SYNC_TASK
import dev.yidafu.blog.common.modal.SyncTaskModel
import dev.yidafu.blog.common.modal.SyncTaskStatus
import dev.yidafu.blog.common.services.BaseService
import org.jooq.CloseableDSLContext
import org.jooq.impl.DSL.concat
import org.koin.core.annotation.Single
import org.mapstruct.factory.Mappers

@Single
class SyncTaskService(
  private val context:CloseableDSLContext,
) : BaseService(context) {
  private val syncTaskConvertor = Mappers.getMapper(SyncTaskConvertor::class.java)
  suspend fun createSyncTask(uuid: String): Boolean {
    val taskRecord = context.newRecord(B_SYNC_TASK)
    taskRecord.uuid = uuid
    taskRecord.status = SyncTaskStatus.Created.ordinal
    taskRecord.callbackurl =  Routes.SYNC_LOG_URL.replace(":uuid", uuid)



    return taskRecord.store() > 0
  }

  suspend fun appendLog(uuid: String, log: String): Boolean = runDB {
    context.update(B_SYNC_TASK).set(
      B_SYNC_TASK.LOGS,
      concat(B_SYNC_TASK.LOGS.name, log),
    ).where(B_SYNC_TASK.UUID.eq(uuid)).execute()

    true
  }

  suspend fun changeStatus(uuid: String, status: SyncTaskStatus): Boolean = runDB{
    context.update(B_SYNC_TASK).set(
      B_SYNC_TASK.STATUS, status.ordinal,
    ).where(B_SYNC_TASK.UUID.eq(uuid)).execute()

    true
  }

  suspend fun getSyncLog(uuid: String): SyncTaskModel? = runDB{
    val taskRecord = context.selectFrom(B_SYNC_TASK).where(B_SYNC_TASK.UUID.eq(uuid)).fetchOne()
    syncTaskConvertor.recordToModal(taskRecord)
  }
}
