package dev.yidafu.blog.admin.jobs

import dev.yidafu.blog.admin.manager.SynchronousManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.Koin
import org.koin.java.KoinJavaComponent.getKoin
import org.quartz.Job
import org.quartz.JobExecutionContext
import org.slf4j.LoggerFactory

class SynchronousJob : Job {
  private val log = LoggerFactory.getLogger(SynchronousJob::class.java)

  override fun execute(p0: JobExecutionContext?) {
    val koin: Koin = getKoin()
    log.info("execute jobs!")
    CoroutineScope(Dispatchers.IO).launch {
      val synchronousManager = koin.get<SynchronousManager>()
      synchronousManager.startSync()
    }
  }

  companion object {
    const val NAME = "SynchronousJob"
    const val GROUP = "SynchronousGroup"
    const val TRIGGER = "syncTrigger"
  }
}
