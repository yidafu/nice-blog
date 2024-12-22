package dev.yidafu.blog.common

import jdk.jfr.Timespan.MILLISECONDS
import org.slf4j.Logger
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeUnit.MILLISECONDS

interface Cache {
  fun get(key: String): String?
  fun put(key: String, value: String)
}

class TimedCache(var log: Logger) : Cache {
  private var cacheTimeValidityInMillis: Long = 0
  private val hashMap = ConcurrentHashMap<String, TimedEntry>()

  companion object {
//    fun expiringEvery(duration: Long, timeUnit: TimeUnit) =
//      TimedCache().apply {
//        cacheTimeValidityInMillis = MILLISECONDS.convert(duration, timeUnit)
//      }
  }

  override fun get(key: String): String? {
    val timedEntry = hashMap[key]
    if (timedEntry == null || timedEntry.isExpired()) {
      log.debug("cache miss for key $key")
      return null
    }

    return timedEntry.value
  }

  override fun put(key: String, value: String) {
    log.debug("caching $key with value $value")
    hashMap[key] = TimedEntry(value, cacheTimeValidityInMillis)
  }



  data class TimedEntry(val value: String, val maxDurationInMillis: Long) {
    private val creationTime: Long = now()

    fun isExpired() = (now() - creationTime) > maxDurationInMillis

    private fun now() = System.currentTimeMillis()
  }
}
