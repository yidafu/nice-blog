package dev.yidafu.blog.themes

import dev.yidafu.blog.themes.cache.CacheManager


interface PageProvider {
  fun getName(): String

  fun createPage(modal: DataModal): Page
}

private val cacheManager = CacheManager<Page>()

abstract class CacheablePageProvider : PageProvider {
   fun hasPage(uuid: String): Boolean {
    return cacheManager.has(uuid)
  }

  private val env: String by lazy{
    System.getenv("BLOG_ENV")
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
