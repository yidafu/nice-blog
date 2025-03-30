package dev.yidafu.blog.themes.blank

import com.google.auto.service.AutoService
import dev.yidafu.blog.themes.*

@AutoService(TemplateManager::class)
class BlankTemplateManager : BaseTemplateManager() {

  init {
    registerPageProvider(ArticleListPageProvider())
    registerPageProvider(AboutMePageProvider())
    registerPageProvider(ArticleDetailPageProvider())
  }

  override fun getName(): String = "Blank Theme"

  override fun getDescription(): String = "Blog without any decoration"
}
