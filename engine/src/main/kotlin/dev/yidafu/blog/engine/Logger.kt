package dev.yidafu.blog.dev.yidafu.blog.engine

interface Logger {
  suspend fun log(
    taskId: String,
    str: String,
  )
}

class StdLogger() : Logger {
  override suspend fun log(
    taskId: String,
    str: String,
  ) {
    println("[Std Output]<$taskId>: $str")
  }
}
