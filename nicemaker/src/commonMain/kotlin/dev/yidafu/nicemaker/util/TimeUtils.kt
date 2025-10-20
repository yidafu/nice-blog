package dev.yidafu.nicemaker.util

import kotlinx.datetime.Clock

object TimeUtils {
  fun currentTimeMillis(): Long {
    return Clock.System.now().toEpochMilliseconds()
  }
}
