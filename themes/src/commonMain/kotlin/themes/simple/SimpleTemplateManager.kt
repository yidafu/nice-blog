package dev.yidafu.nicemaker.themes.simple

import dev.yidafu.nicemaker.themes.BaseTemplateManager
import dev.yidafu.nicemaker.themes.TemplateManager
import dev.yidafu.nicemaker.themes.simple.pages.error.Error404PageProvider
import dev.yidafu.nicemaker.themes.simple.pages.error.Error500PageProvider
import dev.yidafu.nicemaker.themes.simple.pages.front.*

class SimpleTemplateManager : BaseTemplateManager() {
  override fun getName(): String = NAME

  init {
    println("init simple template manager")

    // 前台页面
    registerPageProvider(ArticleListPageProvider())
    registerPageProvider(ArticleDetailPageProvider())
    registerPageProvider(AboutMePageProvider())
    registerPageProvider(TagListPageProvider())
    registerPageProvider(SeriesListPageProvider())
    registerPageProvider(SeriesDetailPageProvider())

    // 错误页面
    registerPageProvider(Error404PageProvider())
    registerPageProvider(Error500PageProvider())
  }

  override fun getDescription(): String = "Simple Theme"

  companion object {
    const val NAME = "Simple Theme"
  }
}
