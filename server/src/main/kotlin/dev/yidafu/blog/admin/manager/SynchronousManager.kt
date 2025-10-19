package dev.yidafu.blog.admin.manager

import dev.yidafu.blog.admin.services.SyncTaskService
import dev.yidafu.blog.common.ConfigurationKeys
import dev.yidafu.blog.common.ext.getByKey
import dev.yidafu.blog.common.services.ConfigurationService
import dev.yidafu.blog.engine.*
import dev.yidafu.blog.common.annotation.Service
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.slf4j.LoggerFactory
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

interface SynchronousManager {
  suspend fun startSync(forceSync: Boolean): String
}

@Service
class SynchronousManagerImpl(
  private val configService: ConfigurationService,
  private val syncTaskService: SyncTaskService,
) : SynchronousManager {
  private val log = LoggerFactory.getLogger(SynchronousManager::class.java)

  @OptIn(ExperimentalUuidApi::class)
  override suspend fun startSync(forceSync: Boolean): String {
    val taskUuid = Uuid.random().toHexString()
    log.info("start synchronous task {}", taskUuid)

    syncTaskService.createSyncTask(taskUuid)

    val count = syncTaskService.getRunningTaskCount()
    // if there is any task running, execute current task after preview task end
    if (count == 0) {
      executeTask(taskUuid, forceSync)
    }
    return taskUuid
  }

  private suspend fun executeTask(
    taskUuid: String,
    forceSync: Boolean,
  ) {
    val configs =
      configService.getByKeys(
        listOf(
          ConfigurationKeys.SOURCE_BRANCH,
          ConfigurationKeys.SOURCE_URL,
        ),
      )
    val gitUrl = configs.getByKey(ConfigurationKeys.SOURCE_URL) ?: throw IllegalStateException("Git url can't be null")
    val gitBranch = configs.getByKey(ConfigurationKeys.SOURCE_BRANCH) ?: throw IllegalStateException("Git branch can't be null")

    withContext(Dispatchers.IO) {
      // 直接创建所需的对象，不使用Koin
      val config = GitConfig(gitUrl, gitBranch, uuid = taskUuid, forceSync = forceSync)
      val logger = DBLogger(config)
      val articleManager = DBArticleManager(logger, config)
      val listener = DBSynchronousListener(config, logger)
      val writer = LogWriter(logger)

      // 创建并执行同步任务
      val syncTask = GitSynchronousTask(config, listener, articleManager, logger, writer, configService)
      syncTask.sync()

      // 检查是否有其他任务需要执行
      if (syncTaskService.getRunningTaskCount() > 0) {
        val task = syncTaskService.findLatestRunningTask()
        task.uuid?.let {
          executeTask(it, task.forceSync ?: false)
        }
      }
    }
  }
}
