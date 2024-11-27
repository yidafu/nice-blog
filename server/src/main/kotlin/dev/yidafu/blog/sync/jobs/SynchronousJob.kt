package dev.yidafu.blog.sync.jobs

import org.quartz.Job
import org.quartz.JobExecutionContext
import org.slf4j.LoggerFactory

class SynchronousJob : Job {
  private val log = LoggerFactory.getLogger(SynchronousJob::class.java)
  override fun execute(p0: JobExecutionContext?) {
    log.info("execute jobs!")
  }
  companion object {
    const val NAME = "SynchronousJob"
    const val GROUP = "SynchronousGroup"
    const val TRIGGER = "syncTrigger"
  }
}
