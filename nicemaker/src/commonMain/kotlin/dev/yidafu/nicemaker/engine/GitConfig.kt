package dev.yidafu.nicemaker.engine

import kotlinx.io.files.Path

/**
 * Git配置 - KMP兼容版本
 * 使用字符串解析代替java.net.URL
 */
data class GitConfig(
  var url: String = "",
  var branch: String = "",
  var localPath: String = DEFAULT_REPO_LOCATION,
  val uuid: String = "",
  val forceSync: Boolean = false,
) {
  fun getLocalRepoPath(): Path {
    if (localPath == DEFAULT_REPO_LOCATION) {
      // 手动解析URL路径，不依赖java.net.URL
      val repoName = url.substringAfterLast('/').replace(".git", "")
      return Path(repoName)
    }
    return Path(localPath)
  }

  companion object {
    const val DEFAULT_REPO_LOCATION = "@defaultRepo@"
  }
}

