package dev.yidafu.nicemaker.common.utils

import kotlinx.datetime.Clock

object TimeUtils {
  fun currentTimeMillis(): Long {
    return Clock.System.now().toEpochMilliseconds()
  }
}
