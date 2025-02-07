package dev.yidafu.blog.engine

import dev.yidafu.blog.common.dao.tables.references.B_SYNC_TASK
import dev.yidafu.blog.engine.Logger
import dev.yidafu.blog.engine.TaskScope
import dev.yidafu.blog.engine.config.GitConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import org.jooq.CloseableDSLContext
import org.jooq.impl.DSL.concat
import org.koin.core.annotation.Scope
import org.koin.core.annotation.Scoped
import org.slf4j.LoggerFactory

@Scope(name = TaskScope.NAME)
@Scoped
class DBLogger(
  config: GitConfig,
  private val context: CloseableDSLContext,
) : Logger(config) {
  private val log = LoggerFactory.getLogger(DBLogger::class.java)
  private val flow = MutableSharedFlow<String>(5, 100, BufferOverflow.SUSPEND)

  init {
    CoroutineScope(Dispatchers.IO).launch {
      flow.collect {
        val res =
          context.update(B_SYNC_TASK).set(
            B_SYNC_TASK.LOGS,
            concat(B_SYNC_TASK.LOGS, it + "\n"),
          ).where(B_SYNC_TASK.UUID.eq(taskId)).execute()
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
