package dev.yidafu.nicemaker.platform.process

import kotlinx.io.files.Path

/**
 * 跨平台进程执行工具
 * - JVM, JS, Linux, macOS: 使用 io.matthewnelson.kmp-process 库
 * - Windows (mingwX64): 手动实现
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
