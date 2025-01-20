package dev.yidafu.blog.dev.yidafu.blog.engine

interface Logger {
  suspend fun log(str: String)
}

class StdLogger() : Logger {
  override suspend fun log(str: String) {
    println("[Std Output]: $str")
  }
}
