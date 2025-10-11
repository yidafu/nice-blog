package dev.yidafu.blog.engine

import dev.yidafu.blog.common.annotation.Service
import org.slf4j.LoggerFactory

interface SynchronousListener {
  fun onStart()

  fun onFinish()

  fun onFailed(e: Exception)
}

@Service("defaultListener")
class DefaultSynchronousListener : SynchronousListener {
  private val log = LoggerFactory.getLogger(SynchronousListener::class.java)

  override fun onStart() {}

  override fun onFinish() {}

  override fun onFailed(e: Exception) {
    log.error("sync failed", e)
  }
}
