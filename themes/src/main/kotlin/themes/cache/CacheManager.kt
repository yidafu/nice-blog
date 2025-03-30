package dev.yidafu.blog.themes.cache

import java.util.concurrent.ConcurrentHashMap
import kotlin.time.Duration.Companion.hours

class CacheManager<T> {

  data class CacheEntry<T>(
    var value: T,
    val expirationTime: Long = System.currentTimeMillis() + DEFAULT_EXPIRE,
  ) {
    companion object {
      val DEFAULT_EXPIRE = 72.hours.inWholeMilliseconds
    }
  }

  internal val cache = ConcurrentHashMap<String, CacheEntry<T>>()

  fun has(key: String): Boolean {
    val entry = cache[key] ?: return false

    return if (System.currentTimeMillis() > entry.expirationTime) {
      cache.remove(key)
      false
    } else {
      return true
    }
  }

  fun get(key: String): T? {
    return if (has(key)) {
      cache[key]?.value
    } else {
      null
    }
  }

  fun set(key: String, value: T) {
    cache[key] = CacheEntry(value)
  }

  fun reset() {
    cache.clear()
  }
}
