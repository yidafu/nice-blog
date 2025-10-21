package dev.yidafu.nicemaker.platform.process

import io.matthewnelson.kmp.process.Process
import kotlinx.io.files.Path

/**
 * JVM 平台使用 kmp-process 实现
 */
actual object ProcessUtils {
  actual suspend fun executeGitCommand(
    vararg command: String,
    workingDir: Path?
  ): ProcessResult {
    // 如果有工作目录，构建带 cd 的完整命令
    val fullCommand = if (workingDir != null) {
      // 使用 shell 执行，先 cd 再执行命令
      val cmdStr = command.joinToString(" ")
      listOf("sh", "-c", "cd \"$workingDir\" && $cmdStr")
    } else {
      command.toList()
    }

    // 使用 kmp-process 执行命令并获取输出
    val builder = Process.Builder(fullCommand[0])
      .args(*fullCommand.drop(1).toTypedArray())

    // 执行并获取输出
    val output = builder.output()

    // 合并 stdout 和 stderr
    val allOutput = (output.stdout + output.stderr)
      .lines()
      .filter { it.isNotBlank() }

    return ProcessResult(
      resultCode = output.processInfo.exitCode,
      output = allOutput
    )
  }
}

