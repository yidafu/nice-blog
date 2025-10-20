package dev.yidafu.nicemaker.util.extension

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime

val format =
  LocalDateTime.Format {
    year()
    char('-')
    monthNumber()
    char('-')
    dayOfMonth()
    char(' ')
    hour()
    char(':')
    minute()
    char(':')
    second()
  }

fun LocalDateTime?.formatString(): String {
  if (this == null) return "-"

  return this.format(format)
}

fun LocalDateTime.Companion.now(): LocalDateTime {
  return Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
}
