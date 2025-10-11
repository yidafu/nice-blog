package dev.yidafu.blog.admin.jobs

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.quartz.Job
import org.quartz.JobExecutionContext
import org.slf4j.LoggerFactory

class SynchronousJob : Job {
  private val log = LoggerFactory.getLogger(SynchronousJob::class.java)

  override fun execute(p0: JobExecutionContext?) {
    log.info("execute jobs!")
    // In a real application, you would need a proper way to access the DI container in Quartz jobs
    // One approach is to create a companion object or a global reference to the DI container
    // For this example, we'll create a simple global DI registry
    CoroutineScope(Dispatchers.IO).launch {
//      val synchronousManager = DIRegistry.synchronousManager
//      synchronousManager?.startSync(false) ?: log.error("Failed to get SynchronousManager from DI registry")
    }
  }

  companion object {
    const val NAME = "SynchronousJob"
    const val GROUP = "SynchronousGroup"
    const val TRIGGER = "syncTrigger"
  }
}
