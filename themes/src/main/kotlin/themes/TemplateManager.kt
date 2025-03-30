package dev.yidafu.blog.themes

interface TemplateManager {
  /**
   * get template name. must be unique name
   */
  fun getName(): String

  /**
   * get template description
   */
  fun getDescription(): String

  fun registerPageProvider(pageProvider: PageProvider)

  fun getPageProvider(name: String): PageProvider?

//  fun createArticleListPage(modal: DataModal): Page
//
//  fun getArticleDetailPage(modal: DataModal): Page
//
//  fun getAboutMePage(modal: DataModal): Page
}
