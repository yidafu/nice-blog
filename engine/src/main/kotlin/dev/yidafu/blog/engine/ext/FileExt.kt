package dev.yidafu.blog.dev.yidafu.blog.engine

import java.nio.file.Path
import java.time.LocalDateTime
import java.util.*
import kotlin.io.path.absolutePathString
import kotlin.io.path.getLastModifiedTime

fun Path.toSafePath(): String {
  return absolutePathString().replace(" ", "\\ ")
}

fun Path.getGitModifyTime(): LocalDateTime {
  val workingDirectory = parent.toFile()
  val timeStr = "git log -1 --pretty=format:\"%ad\" --date iso -- ${toSafePath()}".runCommand(workingDirectory)
  if (timeStr.isBlank()) {
    return LocalDateTime.ofInstant(getLastModifiedTime().toInstant(), TimeZone.getDefault().toZoneId())
  }

  return LocalDateTime.parse(timeStr)
}

fun Path.getGitCreateTime(): LocalDateTime {
  val workingDirectory = parent.toFile()
  val cmd = "git log --pretty=format:'%ad' --date iso -- ${toSafePath()} | tail -1"
  val timeStr = cmd.runCommand(workingDirectory)
  if (timeStr.isBlank()) {
    return LocalDateTime.ofInstant(getLastModifiedTime().toInstant(), TimeZone.getDefault().toZoneId())
  }

  return LocalDateTime.parse(timeStr)
}
