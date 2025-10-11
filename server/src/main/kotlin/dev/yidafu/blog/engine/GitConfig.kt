package dev.yidafu.blog.engine

import dev.yidafu.blog.common.annotation.Service
import java.io.File
import java.net.URL

@Service
data class GitConfig(
  var url: String = "",
  var branch: String = "",
  var localPath: String = DEFAULT_REPO_LOCATION,
  val uuid: String = "",
  val forceSync: Boolean = false,
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
