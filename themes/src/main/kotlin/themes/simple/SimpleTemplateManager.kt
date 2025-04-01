package dev.yidafu.blog.themes.simple

import com.google.auto.service.AutoService
import dev.yidafu.blog.themes.BaseTemplateManager
import dev.yidafu.blog.themes.TemplateManager
import dev.yidafu.blog.themes.simple.pages.front.AboutMePageProvider
import dev.yidafu.blog.themes.simple.pages.front.ArticleDetailPageProvider
import dev.yidafu.blog.themes.simple.pages.front.ArticleListPageProvider
import dev.yidafu.blog.themes.simple.pages.admin.AdminLoginPageProvider
import dev.yidafu.blog.themes.simple.pages.admin.AdminDashboardPageProvider
import dev.yidafu.blog.themes.simple.pages.admin.article.AdminArticleDetailPageProvider
import dev.yidafu.blog.themes.simple.pages.admin.article.AdminArticleListPageProvider
import dev.yidafu.blog.themes.simple.pages.admin.config.AdminConfigAppearancePageProvider
import dev.yidafu.blog.themes.simple.pages.admin.config.AdminConfigDataSourcePageProvider
import dev.yidafu.blog.themes.simple.pages.admin.config.AdminConfigSyncPageProvider
import dev.yidafu.blog.themes.simple.pages.admin.sync.AdminSyncLogDetailPageProvider
import dev.yidafu.blog.themes.simple.pages.admin.sync.AdminSyncLogListPageProvider
import dev.yidafu.blog.themes.simple.pages.admin.sync.AdminSyncOperatePageProvider

@AutoService(TemplateManager::class)
class SimpleTemplateManager : BaseTemplateManager() {
  override fun getName(): String = NAME

  init {
    println("init simple template manager")
    registerPageProvider(ArticleListPageProvider())
    registerPageProvider(ArticleDetailPageProvider())
    registerPageProvider(AboutMePageProvider())

    // admin pages
    registerPageProvider(AdminLoginPageProvider())

    registerPageProvider(AdminDashboardPageProvider())
    registerPageProvider(AdminArticleListPageProvider())
    registerPageProvider(AdminArticleDetailPageProvider())
    registerPageProvider(AdminConfigAppearancePageProvider())
    registerPageProvider(AdminConfigDataSourcePageProvider())
    registerPageProvider(AdminConfigSyncPageProvider())

    registerPageProvider(AdminSyncLogListPageProvider())
    registerPageProvider(AdminSyncLogDetailPageProvider())
    registerPageProvider(AdminSyncOperatePageProvider())
  }

  override fun getDescription(): String = "Default Template"

  companion object {
    const val NAME = "Simple Theme"
  }
}
