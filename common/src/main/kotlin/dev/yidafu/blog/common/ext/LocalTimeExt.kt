package dev.yidafu.blog.common.ext

import java.time.LocalDate
import java.time.LocalTime
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

internal var formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm")

fun LocalTime?.formatString(): String {
  if (this == null) return "-"

  return formatter.format(this)
}

fun LocalDate?.formatString(): String {
  if (this == null) return "-"

  return formatter.format(this)
}
fun LocalDateTime?.formatString(): String {
  if (this == null) return "-"

  return formatter.format(this)
}
