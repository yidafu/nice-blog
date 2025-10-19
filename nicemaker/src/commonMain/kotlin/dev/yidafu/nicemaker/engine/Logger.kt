package dev.yidafu.nicemaker.engine

interface Logger {
  suspend fun log(str: String)

  fun logSync(str: String)
}

abstract class BaseLogger(config: GitConfig) : Logger {
  val taskId: String = config.uuid
}

class StdLogger(
  config: GitConfig,
) : BaseLogger(config) {
  override suspend fun log(str: String) {
    println("[Std Output]<$taskId>: $str")
  }

  override fun logSync(str: String) {
    println("[Std Output]<$taskId>: $str")
  }
}

