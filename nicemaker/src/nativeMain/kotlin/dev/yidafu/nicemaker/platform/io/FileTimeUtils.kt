package dev.yidafu.nicemaker.platform.io

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.io.files.Path
import platform.posix.stat
import platform.posix.stat as posixStat

/**
 * Native平台的文件时间工具实现
 * 使用platform.posix.stat获取文件时间
 */
@OptIn(ExperimentalForeignApi::class)
actual object FileTimeUtils {
  actual fun getCreationTime(path: Path): LocalDateTime? {
    // TODO: Native平台stat结构体字段在不同系统上有差异
    // 暂时返回null，使用fallback值
    // 未来可以为每个平台（macosArm64Main, macosX64Main等）创建特定实现
    return null
  }

  actual fun getModifiedTime(path: Path): LocalDateTime? {
    // TODO: Native平台stat结构体字段在不同系统上有差异
    // 暂时返回null，使用fallback值
    return null
  }
}

