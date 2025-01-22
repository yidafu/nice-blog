package dev.yidafu.blog.admin.services

import dev.yidafu.blog.common.Routes
import dev.yidafu.blog.common.converter.SyncTaskConvertor
import dev.yidafu.blog.common.dao.tables.references.B_SYNC_TASK
import dev.yidafu.blog.common.modal.SyncTaskModel
import dev.yidafu.blog.common.modal.SyncTaskStatus
import dev.yidafu.blog.common.query.PageQuery
import dev.yidafu.blog.common.services.BaseService
import org.jooq.CloseableDSLContext
import org.koin.core.annotation.Single
import org.mapstruct.factory.Mappers

@Single
class SyncTaskService(
  private val context: CloseableDSLContext,
) : BaseService(context) {
  private val syncTaskConvertor = Mappers.getMapper(SyncTaskConvertor::class.java)

  suspend fun createSyncTask(uuid: String): Boolean {
    val taskRecord = context.newRecord(B_SYNC_TASK)
    taskRecord.uuid = uuid
    taskRecord.status = SyncTaskStatus.Created.ordinal
    taskRecord.callbackUrl = Routes.SYNC_API_LOG_URL.replace(":uuid", uuid)

    return taskRecord.store() > 0
  }

  suspend fun changeStatus(
    uuid: String,
    status: SyncTaskStatus,
  ): Boolean =
    runDB {
      context.update(B_SYNC_TASK).set(
        B_SYNC_TASK.STATUS,
        status.ordinal,
      ).where(B_SYNC_TASK.UUID.eq(uuid)).execute()

      true
    }

  suspend fun getSyncLog(uuid: String): SyncTaskModel =
    runDB {
      val taskRecord = context.selectFrom(B_SYNC_TASK).where(B_SYNC_TASK.UUID.eq(uuid)).fetchOne()
      syncTaskConvertor.recordToModal(taskRecord)
    }

  suspend fun getSyncLogs(query: PageQuery): Pair<Int, List<SyncTaskModel>> =
    runDB {
      val logCount = context.selectCount().from(B_SYNC_TASK).fetchOne(0, Int::class.java) ?: 0
      val taskRecords =
        context.selectFrom(B_SYNC_TASK)
          .limit(query.size)
          .offset(query.size * (query.page - 1))
          .fetchArray()
      val list = syncTaskConvertor.recordToModal(taskRecords.toList())

      logCount to list
    }
}
