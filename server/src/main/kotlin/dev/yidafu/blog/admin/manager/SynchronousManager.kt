package dev.yidafu.blog.admin.manager

import dev.yidafu.blog.admin.services.SyncTaskService
import dev.yidafu.blog.common.ConfigurationKeys
import dev.yidafu.blog.common.ext.getByKey
import dev.yidafu.blog.common.services.ConfigurationService
import dev.yidafu.blog.engine.BaseGitSynchronousTask
import dev.yidafu.blog.engine.DBLogger
import dev.yidafu.blog.engine.DBSynchronousListener
import dev.yidafu.blog.engine.GitConfig
import dev.yidafu.blog.engine.Logger
import dev.yidafu.blog.engine.SynchronousListener
import dev.yidafu.blog.engine.TaskScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Single
import org.koin.core.qualifier.StringQualifier
import org.koin.java.KoinJavaComponent.getKoin
import org.slf4j.LoggerFactory
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Single
class SynchronousManager(
  private val configService: ConfigurationService,
  private val syncTaskService: SyncTaskService,
) {
  private val log = LoggerFactory.getLogger(SynchronousManager::class.java)
  private val koin = getKoin()

  @OptIn(ExperimentalUuidApi::class)
  suspend fun startSync(): String {
    val taskUuid = Uuid.random().toHexString()
    log.info("start synchronous task {}", taskUuid)

    syncTaskService.createSyncTask(taskUuid)

    val count = syncTaskService.getRunningTaskCount()
    // if there is any task running, execute current task after preview task end
    if (count == 0) {
      executeTask(taskUuid)
    }
    return taskUuid
  }

  private suspend fun executeTask(taskUuid: String) {
    val configs =
      configService.getByKeys(
        listOf(
          ConfigurationKeys.SOURCE_BRANCH,
          ConfigurationKeys.SOURCE_URL,
        ),
      )
    val gitUrl = configs.getByKey(ConfigurationKeys.SOURCE_URL) ?: throw IllegalStateException("Git url can't be null")
    val gitBranch =
      configs.getByKey(ConfigurationKeys.SOURCE_BRANCH) ?: throw IllegalStateException("Git url can't be null")
    withContext(Dispatchers.IO) {
      val taskScope = koin.createScope(taskUuid, StringQualifier(TaskScope.NAME), TaskScope::class)
      val config = GitConfig(gitUrl, gitBranch, uuid = taskUuid)
      taskScope.declare(config)
      taskScope.declare<Logger>(DBLogger(config, taskScope.get()))
      taskScope.declare<SynchronousListener>(DBSynchronousListener(taskScope.get(), taskScope.get(), taskScope.get()))
      val syncTask: BaseGitSynchronousTask = taskScope.get<BaseGitSynchronousTask>()
      syncTask.sync()
      taskScope.close()
      // when current task end, check weather there are 'created' tasks. running blocking sync task
      if (syncTaskService.getRunningTaskCount() > 0) {
        val task = syncTaskService.findLatestRunningTask()
        task.uuid?.let { uuid ->
          executeTask(uuid)
        }
      }
    }
  }
}
