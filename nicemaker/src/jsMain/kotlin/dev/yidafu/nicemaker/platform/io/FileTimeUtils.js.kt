package dev.yidafu.nicemaker.platform.io

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.io.files.Path
import kotlin.js.Date

/**
 * JS 平台的 FileTimeUtils 实现
 */
actual object FileTimeUtils {
  /**
   * 获取文件创建时间
   */
  actual fun getCreationTime(path: Path): LocalDateTime? {
    return try {
      val fs = js("require('fs')")
      val stats = fs.statSync(path.toString())
      val birthtime = stats.birthtime as Date
      kotlinx.datetime.Instant.fromEpochMilliseconds(birthtime.getTime().toLong())
        .toLocalDateTime(TimeZone.currentSystemDefault())
    } catch (e: Exception) {
      null
    }
  }

  /**
   * 获取文件最后修改时间
   */
  actual fun getModifiedTime(path: Path): LocalDateTime? {
    return try {
      val fs = js("require('fs')")
      val stats = fs.statSync(path.toString())
      val mtime = stats.mtime as Date
      kotlinx.datetime.Instant.fromEpochMilliseconds(mtime.getTime().toLong())
        .toLocalDateTime(TimeZone.currentSystemDefault())
    } catch (e: Exception) {
      null
    }
  }
}

