package dev.yidafu.blog.dev.yidafu.blog.engine

import org.slf4j.LoggerFactory
import java.io.File
import java.net.URL

abstract class SyncContext {
  abstract fun log(str: String)

  abstract fun onStart()

  abstract fun onFinish()

  abstract fun onFailed()

  open val gitConfig: GitConfig = GitConfig()

  data class GitConfig(
    var url: String = "",
    var branch: String = "",
    var localPath: String = DEFAULT_REPO_LOCATION,
  ) {
    fun getLocalRepoFile(): File  {
      if (localPath == DEFAULT_REPO_LOCATION) {
        val repoName = URL(url).path.replace(".git", "").substringAfterLast('/')
        return File(repoName)
      }
      return File(localPath)
    }

    companion object {
      const val DEFAULT_REPO_LOCATION = "@defaultRepo@"
    }
  }
}

class LocalSyncContext(
  override val gitConfig: GitConfig = GitConfig(),
) : SyncContext() {
  private val log = LoggerFactory.getLogger(LocalSyncContext::class.java)

  override fun log(str: String) {
    log.info(str)
  }

  override fun onStart() {
  }

  override fun onFinish() {
  }

  override fun onFailed() {
  }
}
