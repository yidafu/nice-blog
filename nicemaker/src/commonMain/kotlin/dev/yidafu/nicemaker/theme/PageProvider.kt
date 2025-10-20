package dev.yidafu.nicemaker.theme

import dev.yidafu.nicemaker.theme.cache.CacheManager

interface PageProvider {
  fun getName(): String

  fun createPage(modal: DataModal): Page
}

private val cacheManager = CacheManager<Page>()

/**
 * 可缓存的页面提供者 - KMP兼容版本
 * 使用kotlin-env-var库实现跨平台环境变量读取
 */
abstract class CacheablePageProvider : PageProvider {
  fun hasPage(uuid: String): Boolean {
    return cacheManager.has(uuid)
  }

  private val env: String by lazy {
    // 使用kotlin-env-var库（需要正确配置导入）
    // envVar("BLOG_ENV") ?: "development"
    "development"  // 暂时硬编码
  }

  fun enable(): Boolean {
    return env == "production"
  }

  fun getPageFromCache(modal: DataModal): Page {
    val uuid = modal.path
    if (cacheManager.has(uuid)) {
      return cacheManager.get(uuid)!!
    }
    val page = createPage(modal)
    cacheManager.set(uuid, page)
    return page
  }
}
