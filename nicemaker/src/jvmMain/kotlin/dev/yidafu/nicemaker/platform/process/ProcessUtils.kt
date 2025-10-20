package dev.yidafu.nicemaker.platform.process

import io.matthewnelson.kmp.process.Process
import kotlinx.io.files.Path
import java.io.File

actual object ProcessUtils {
  actual suspend fun executeGitCommand(
    vararg command: String,
    workingDir: Path?
  ): ProcessResult {
    // 使用kmp-process执行命令并获取输出
    val builder = Process.Builder(command[0])
      .args(*command.drop(1).toTypedArray())

    // 设置工作目录
    if (workingDir != null) {
      builder.chdir(File(workingDir.toString()))
    }

    // 执行并获取输出
    val output = builder.output()

    // 合并stdout和stderr
    val allOutput = (output.stdout + output.stderr)
      .lines()
      .filter { it.isNotBlank() }

    return ProcessResult(
      resultCode = output.processInfo.exitCode,
      output = allOutput
    )
  }
}
