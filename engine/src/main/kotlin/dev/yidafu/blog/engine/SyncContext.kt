package dev.yidafu.blog.dev.yidafu.blog.engine

import dev.yidafu.blog.dev.yidafu.blog.engine.TaskScope.Companion.NAME
import org.koin.core.annotation.Scope
import org.koin.core.annotation.Scoped
import org.slf4j.LoggerFactory
import java.io.File
import java.net.URL

@Scope(name = NAME)
@Scoped
data class GitConfig(
  var url: String = "",
  var branch: String = "",
  var localPath: String = DEFAULT_REPO_LOCATION,
  val uuid: String = "",
) {
  fun getLocalRepoFile(): File {
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

interface SynchronousListener {
  fun onStart()

  fun onFinish()

  fun onFailed(e: Exception)
}

@Scope(name = NAME)
@Scoped
class DefaultSynchronousListener : SynchronousListener {
  private val log = LoggerFactory.getLogger(SynchronousListener::class.java)
  override fun onStart() {
  }

  override fun onFinish() {
  }

  override fun onFailed(e: Exception) {
    log.error("sync failed", e)
  }
}
