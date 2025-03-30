package dev.yidafu.blog.themes.cache

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlin.time.Duration.Companion.hours

class CacheManagerTest : StringSpec({
  "should return false when key does not exist" {
    val cacheManager = CacheManager<String>()
    cacheManager.has("nonexistentKey") shouldBe false
  }

  "should return true when key exists and has not expired" {
    val cacheManager = CacheManager<String>()
    cacheManager.set("key", "value")
    cacheManager.has("key") shouldBe true
  }

  "should return false when key exists but has expired" {
    val cacheManager = CacheManager<String>()
    val expiredEntry = CacheManager.CacheEntry("value", System.currentTimeMillis() - 1.hours.inWholeMilliseconds)
    cacheManager.cache["expiredKey"] = expiredEntry
    cacheManager.has("expiredKey") shouldBe false
  }

  "should return value when key exists and has not expired" {
    val cacheManager = CacheManager<String>()
    cacheManager.set("key", "value")
    cacheManager.get("key") shouldBe "value"
  }

  "should return null when key does not exist" {
    val cacheManager = CacheManager<String>()
    cacheManager.get("nonexistentKey") shouldBe null
  }

  "should return null when key exists but has expired" {
    val cacheManager = CacheManager<String>()
    val expiredEntry = CacheManager.CacheEntry("value", System.currentTimeMillis() - 1.hours.inWholeMilliseconds)
    cacheManager.cache["expiredKey"] = expiredEntry
    cacheManager.get("expiredKey") shouldBe null
  }

  "should set value correctly" {
    val cacheManager = CacheManager<String>()
    cacheManager.set("key", "value")
    cacheManager.cache["key"]?.value shouldBe "value"
  }

  "should reset cache correctly" {
    val cacheManager = CacheManager<String>()
    cacheManager.set("key", "value")
    cacheManager.reset()
    cacheManager.cache.isEmpty() shouldBe true
  }
})
