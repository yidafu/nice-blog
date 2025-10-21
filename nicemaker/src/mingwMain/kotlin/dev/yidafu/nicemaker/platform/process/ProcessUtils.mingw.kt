package dev.yidafu.nicemaker.platform.process

import kotlinx.cinterop.*
import kotlinx.io.files.Path
import platform.posix.*

/**
 * Windows (mingwX64) 平台手动实现
 * 使用 Windows posix system() 调用
 */
@OptIn(ExperimentalForeignApi::class)
actual object ProcessUtils {
  actual suspend fun executeGitCommand(
    vararg command: String,
    workingDir: Path?
  ): ProcessResult {
    // 构建完整命令
    val cmdString = buildString {
      if (workingDir != null) {
        // Windows 使用 cd /d
        append("cd /d \"$workingDir\" && ")
      }
      append(command.joinToString(" ") { arg ->
        if (arg.contains(" ")) "\"$arg\"" else arg
      })
      // 重定向输出
      append(" 2>&1")
    }

    // 使用 system 执行命令（简化版，不捕获输出）
    val exitCode = system(cmdString)

    // 注意：这个简化实现不捕获输出
    // 在生产环境中，可能需要使用更复杂的实现
    return ProcessResult(
      resultCode = exitCode,
      output = listOf("Command executed: $cmdString")
    )
  }
}

