package dev.yidafu.blog

import dev.yidafu.blog.dev.yidafu.blog.engine.LocalSyncContext
import dev.yidafu.blog.dev.yidafu.blog.engine.LocalSynchronousTask
import dev.yidafu.blog.dev.yidafu.blog.engine.SyncContext

suspend fun main() {
//  LocalSynchronousTask(LocalSyncContext()).sync()
  LocalSynchronousTask(
    LocalSyncContext(
      SyncContext.GitConfig("https://github.com/yidafu/example-blog.git", branch = "master"),
    ),
  ).sync()
}
