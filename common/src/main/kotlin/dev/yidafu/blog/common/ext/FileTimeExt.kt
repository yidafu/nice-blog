package dev.yidafu.blog.common.ext

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.nio.file.attribute.FileTime
import java.util.concurrent.TimeUnit
import kotlin.time.ExperimentalTime

// 基本转换
@OptIn(ExperimentalTime::class)
fun FileTime.toKotlinDateTime(): LocalDateTime {
  return Instant.fromEpochMilliseconds(this.toMillis())
    .toLocalDateTime(TimeZone.currentSystemDefault())
}

// 指定时区
@OptIn(ExperimentalTime::class)
fun FileTime.toKotlinDateTime(timeZone: TimeZone): LocalDateTime {
  return Instant.fromEpochMilliseconds(this.toMillis())
    .toLocalDateTime(timeZone)
}

// 纳秒精度转换
@OptIn(ExperimentalTime::class)
fun FileTime.toKotlinDateTimeWithNanos(timeZone: TimeZone = TimeZone.UTC): LocalDateTime {
  val seconds = this.to(TimeUnit.SECONDS)
  val nanos = this.to(TimeUnit.NANOSECONDS) - seconds * 1_000_000_000
  return Instant.fromEpochSeconds(seconds, nanos)
    .toLocalDateTime(timeZone)
}
