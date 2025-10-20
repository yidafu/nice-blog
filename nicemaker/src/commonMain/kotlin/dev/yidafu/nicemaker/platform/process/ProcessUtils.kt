package dev.yidafu.nicemaker.platform.process

import kotlinx.io.files.Path

/**
 * 跨平台进程执行工具
 * JVM: 使用 io.matthewnelson.kmp-process 库
 * Native: 使用 platform.posix.popen/pclose
 */
expect object ProcessUtils {
  suspend fun executeGitCommand(
    vararg command: String,
    workingDir: Path?
  ): ProcessResult
}

data class ProcessResult(
  val resultCode: Int,
  val output: List<String>
)
