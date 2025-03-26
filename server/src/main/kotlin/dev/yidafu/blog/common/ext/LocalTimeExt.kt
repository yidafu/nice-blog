package dev.yidafu.blog.common.ext

import java.time.LocalTime
import java.time.format.DateTimeFormatter


internal var formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("YYYY-MM-DD HH:mm:ss")

fun LocalTime?.formatString(): String {
  if (this == null) return  "-"

  return formatter.format(this)
}
