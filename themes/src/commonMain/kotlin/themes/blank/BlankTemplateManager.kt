package dev.yidafu.nicemaker.theme.blank

import dev.yidafu.nicemaker.theme.*

class BlankTemplateManager : BaseTemplateManager() {
  init {
    registerPageProvider(ArticleListPageProvider())
    registerPageProvider(AboutMePageProvider())
    registerPageProvider(ArticleDetailPageProvider())
  }

  override fun getName(): String = "Blank Theme"

  override fun getDescription(): String = "Blog without any decoration"
}
