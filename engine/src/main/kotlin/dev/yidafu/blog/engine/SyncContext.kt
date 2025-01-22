package dev.yidafu.blog.dev.yidafu.blog.engine

import java.io.File
import java.net.URL

abstract class SyncContext {
//  abstract fun log(str: String)

  abstract fun onStart()

  abstract fun onFinish()

  abstract fun onFailed()

  open val gitConfig: GitConfig = GitConfig()
}

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

  fun onFailed()
}

class DefaultSynchronousListener : SynchronousListener {
  override fun onStart() {
  }

  override fun onFinish() {
  }

  override fun onFailed() {
  }
}
