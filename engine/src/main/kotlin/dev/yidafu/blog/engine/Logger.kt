package dev.yidafu.blog.engine

import dev.yidafu.blog.engine.TaskScope.Companion.NAME
import dev.yidafu.blog.engine.config.GitConfig
import org.koin.core.annotation.Scope
import org.koin.core.annotation.Scoped

abstract class Logger(config: GitConfig) {
  val taskId: String = config.uuid

  abstract suspend fun log(str: String)

  abstract fun logSync(str: String)
}

@Scope(name = NAME)
@Scoped
class StdLogger(
  config: GitConfig,
) : Logger(config) {
  override suspend fun log(str: String) {
    println("[Std Output]<$taskId>: $str")
  }

  override fun logSync(str: String) {
    println("[Std Output]<$taskId>: $str")
  }
}
