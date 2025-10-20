package dev.yidafu.nicemaker.themes.cache

import dev.yidafu.nicemaker.common.utils.TimeUtils
import kotlin.time.Duration.Companion.hours

/**
 * 缓存管理器 - KMP兼容版本
 * 使用HashMap代替ConcurrentHashMap，使用TimeUtils获取时间
 */
class CacheManager<T> {
  data class CacheEntry<T>(
    var value: T,
    val expirationTime: Long,
  ) {
    companion object {
      val DEFAULT_EXPIRE = 72.hours.inWholeMilliseconds
      
      fun <T> create(value: T): CacheEntry<T> {
        return CacheEntry(value, TimeUtils.currentTimeMillis() + DEFAULT_EXPIRE)
      }
    }
  }

  internal val cache = HashMap<String, CacheEntry<T>>()  // 使用普通HashMap

  fun has(key: String): Boolean {
    val entry = cache[key] ?: return false

    return if (TimeUtils.currentTimeMillis() > entry.expirationTime) {
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

  fun set(
    key: String,
    value: T,
  ) {
    cache[key] = CacheEntry.create(value)
  }

  fun reset() {
    cache.clear()
  }
}
