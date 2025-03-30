package dev.yidafu.blog.themes.simple

import com.google.auto.service.AutoService
import dev.yidafu.blog.themes.*
import dev.yidafu.blog.themes.simple.pages.*
import dev.yidafu.blog.themes.simple.pages.admin.article.AdminArticleListPageProvider
import dev.yidafu.blog.themes.simple.pages.admin.article.AdminArticleDetailPageProvider
import dev.yidafu.blog.themes.simple.pages.admin.config.AdminConfigAppearancePageProvider
import dev.yidafu.blog.themes.simple.pages.admin.config.AdminConfigDataSourcePageProvider
import dev.yidafu.blog.themes.simple.pages.admin.config.AdminConfigSyncPageProvider


@AutoService(TemplateManager::class)
class SimpleTemplateManager : BaseTemplateManager() {

  override fun getName(): String = NAME

  init {
    println("init simple template manager")
    registerPageProvider(ArticleListPageProvider())
    registerPageProvider(ArticleDetailPageProvider())
    registerPageProvider(AboutMePageProvider())

    // admin pages
    registerPageProvider(AdminArticleListPageProvider())
    registerPageProvider(AdminArticleDetailPageProvider())
    registerPageProvider(AdminConfigAppearancePageProvider())
    registerPageProvider(AdminConfigDataSourcePageProvider())
    registerPageProvider(AdminConfigSyncPageProvider())
  }

  override fun getDescription(): String = "Default Template"

  companion object {
    const val NAME = "Simple Theme"
  }
}
