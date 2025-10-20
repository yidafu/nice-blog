package dev.yidafu.nicemaker.common.utils

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.io.files.Path
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.attribute.BasicFileAttributes

/**
 * JVM平台的文件时间工具实现
 * 使用java.nio.file API获取文件时间
 */
actual object FileTimeUtils {
  actual fun getCreationTime(path: Path): LocalDateTime? {
    return try {
      val javaPath = Paths.get(path.toString())
      val attrs = Files.readAttributes(javaPath, BasicFileAttributes::class.java)
      val fileTime = attrs.creationTime()
      Instant.fromEpochMilliseconds(fileTime.toMillis())
        .toLocalDateTime(TimeZone.currentSystemDefault())
    } catch (e: Exception) {
      null
    }
  }

  actual fun getModifiedTime(path: Path): LocalDateTime? {
    return try {
      val javaPath = Paths.get(path.toString())
      val fileTime = Files.getLastModifiedTime(javaPath)
      Instant.fromEpochMilliseconds(fileTime.toMillis())
        .toLocalDateTime(TimeZone.currentSystemDefault())
    } catch (e: Exception) {
      null
    }
  }
}

