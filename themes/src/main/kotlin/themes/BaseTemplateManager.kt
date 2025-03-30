package dev.yidafu.blog.themes

abstract class BaseTemplateManager : TemplateManager {
  private val pages: MutableList<PageProvider> = mutableListOf()

  override fun registerPageProvider(pageProvider: PageProvider) {
    if (!pages.any { p -> p.getName() == pageProvider.getName() }) {
      pages.add(pageProvider)
    }
  }

  override fun getPageProvider(name: String): PageProvider? {
    return pages.firstOrNull { p -> p.getName() == name }
  }

  fun getNotFoundPage(): Page {
    return NotFoundPage()
  }
}
