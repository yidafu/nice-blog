package dev.yidafu.blog.engine

import dev.yidafu.blog.common.db.dao.SyncTaskEntity
import dev.yidafu.blog.common.db.tables.SyncTaskTable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.koin.core.annotation.Scope
import org.koin.core.annotation.Scoped
import org.slf4j.LoggerFactory

@Scope(name = TaskScope.NAME)
@Scoped
class DBLogger(
  config: GitConfig,
) : Logger(config) {
  private val log = LoggerFactory.getLogger(DBLogger::class.java)
  private val flow = MutableSharedFlow<String>(5, 100, BufferOverflow.SUSPEND)

  init {
    CoroutineScope(Dispatchers.IO).launch {
      flow.collect {
        withContext(Dispatchers.IO) {
          transaction {
            val task = SyncTaskEntity.find { SyncTaskTable.uuid eq taskId }.singleOrNull()
            if (task != null) {
              task.logs = "${task.logs}$it\n"
            }
          }
        }
      }
    }
  }

  override suspend fun log(str: String) {
    flow.emit(str)
  }

  override fun logSync(str: String) {
    flow.tryEmit(str)
  }
}
