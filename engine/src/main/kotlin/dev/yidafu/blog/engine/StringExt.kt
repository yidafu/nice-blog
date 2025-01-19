package dev.yidafu.blog.dev.yidafu.blog.engine

import java.io.File
import java.util.concurrent.TimeUnit
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

/**
 * stackoverflow answer
 * @see https://stackoverflow.com/a/41495542
 */
fun String.runCommand(workingDir: File): String {
  return runCommand(workingDir, (1.0).minutes)
}

fun String.runCommand(
  workingDir: File,
  waitFor: Duration,
): String {
  val proc =
    ProcessBuilder(*split(" ").toTypedArray())
      .directory(workingDir)
      .redirectOutput(ProcessBuilder.Redirect.PIPE)
      .redirectError(ProcessBuilder.Redirect.PIPE)
      .start()

  proc.waitFor(waitFor.inWholeSeconds, TimeUnit.SECONDS)

  return proc.inputReader().buffered().readText()
}
