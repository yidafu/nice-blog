package dev.yidafu.blog.admin.services

import dev.yidafu.blog.common.Routes
import dev.yidafu.blog.common.db.dao.SyncTaskEntity
import dev.yidafu.blog.common.db.tables.SyncTaskTable
import dev.yidafu.blog.common.modal.SyncTaskStatus
import dev.yidafu.blog.common.query.PageQuery
import dev.yidafu.blog.common.services.ExposedBaseService
import dev.yidafu.blog.common.annotation.Service
import org.jetbrains.exposed.v1.core.SortOrder
import org.jetbrains.exposed.v1.core.eq

interface SyncTaskService {
  suspend fun createSyncTask(uuid: String): Boolean

  suspend fun getSyncLog(uuid: String): SyncTaskEntity

  suspend fun getSyncLogs(query: PageQuery): Pair<Int, List<SyncTaskEntity>>

  suspend fun getRunningTaskCount(): Int

  suspend fun getCreatedTaskCount(): Int

  suspend fun findLatestRunningTask(): SyncTaskEntity
}

@Service
class SyncTaskServiceImpl : SyncTaskService, ExposedBaseService() {
  /**
   * 创建同步任务
   */
  override suspend fun createSyncTask(uuid: String): Boolean =
    runDB {
      SyncTaskEntity.new {
        this.uuid = uuid
        this.status = SyncTaskStatus.Created
        this.callbackUrl = Routes.SYNC_API_LOG_URL.replace(":uuid", uuid)
        this.logs = "Task created"
        this.forceSync = false
      }
      true
    }

  /**
   * 获取同步日志
   */
  override suspend fun getSyncLog(uuid: String): SyncTaskEntity =
    runDB {
      SyncTaskEntity.find { SyncTaskTable.uuid eq uuid }
        .single()
    }

  /**
   * 分页获取同步日志
   */
  override suspend fun getSyncLogs(query: PageQuery): Pair<Int, List<SyncTaskEntity>> =
    runDB {
      val logCount = SyncTaskEntity.all().count().toInt()
      val taskRecords =
        SyncTaskEntity.all()
          .limit(query.size)
          .offset(query.offset.toLong())
          .orderBy(SyncTaskTable.updatedAt to SortOrder.DESC).toList()

      logCount to taskRecords
    }

  /**
   * 根据状态获取任务数量
   */
  private suspend fun getCountByStatus(status: SyncTaskStatus): Int =
    runDB {
      SyncTaskEntity.find { SyncTaskTable.status eq status }
        .count().toInt()
    }

  /**
   * 获取运行中的任务数量
   */
  override suspend fun getRunningTaskCount(): Int = getCountByStatus(SyncTaskStatus.Running)

  /**
   * 获取已创建的任务数量
   */
  override suspend fun getCreatedTaskCount(): Int = getCountByStatus(SyncTaskStatus.Created)

  /**
   * 查找最新的创建状态任务
   */
  override suspend fun findLatestRunningTask(): SyncTaskEntity =
    runDB {
      SyncTaskEntity.find { SyncTaskTable.status eq SyncTaskStatus.Created }
        .orderBy(SyncTaskTable.createdAt to SortOrder.ASC)
        .limit(1)
        .single()
    }
}
