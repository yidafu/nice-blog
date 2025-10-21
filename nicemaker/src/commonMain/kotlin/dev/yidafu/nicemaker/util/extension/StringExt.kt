package dev.yidafu.nicemaker.util.extension

import dev.yidafu.nicemaker.platform.process.ProcessUtils
import kotlinx.io.files.Path
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

/**
 * 运行命令（使用 ProcessUtils）
 */
suspend fun String.runCommand(workingDir: Path): String {
  return runCommand(workingDir, (1.0).minutes)
}

suspend fun String.runCommand(
  workingDir: Path,
  waitFor: Duration,
): String {
  val result = ProcessUtils.executeGitCommand(*split(" ").toTypedArray(), workingDir = workingDir)
  return result.output.joinToString("\n")
}
