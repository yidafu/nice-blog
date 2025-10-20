package dev.yidafu.nicemaker.engine.process

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.allocArray
import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.toKString
import kotlinx.io.files.Path
import platform.posix.*

/**
 * Native平台（macOS, Linux, Windows）的ProcessUtils实现
 * 使用 platform.posix.popen/pclose
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
        // Unix使用cd, Windows使用cd /d
        append("cd \"$workingDir\" && ")
      }
      append(command.joinToString(" ") { arg ->
        if (arg.contains(" ")) "\"$arg\"" else arg
      })
    }

    // 使用popen执行命令（Windows上使用_popen，Unix上使用popen）
    val fp = try {
      popen(cmdString, "r")
    } catch (e: Exception) {
      return ProcessResult(-1, listOf("Failed to execute command: ${e.message}"))
    }

    if (fp == null) {
      return ProcessResult(-1, listOf("Failed to execute command"))
    }

    val output = mutableListOf<String>()
    memScoped {
      val buffer = allocArray<ByteVar>(4096)
      while (fgets(buffer, 4096, fp) != null) {
        val line = buffer.toKString().trim()
        if (line.isNotBlank()) {
          output.add(line)
        }
      }
    }

    val exitCode = try {
      pclose(fp)
    } catch (e: Exception) {
      -1
    }

    return ProcessResult(
      resultCode = exitCode,
      output = output
    )
  }
}
