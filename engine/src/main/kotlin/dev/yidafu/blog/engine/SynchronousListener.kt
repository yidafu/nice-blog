package dev.yidafu.blog.engine

import dev.yidafu.blog.engine.TaskScope.Companion.NAME
import org.koin.core.annotation.Scope
import org.koin.core.annotation.Scoped
import org.slf4j.LoggerFactory

interface SynchronousListener {
  fun onStart()

  fun onFinish()

  fun onFailed(e: Exception)
}

@Scope(name = NAME)
@Scoped
class DefaultSynchronousListener : SynchronousListener {
  private val log = LoggerFactory.getLogger(SynchronousListener::class.java)

  override fun onStart() {}

  override fun onFinish() {}

  override fun onFailed(e: Exception) {
    log.error("sync failed", e)
  }
}
