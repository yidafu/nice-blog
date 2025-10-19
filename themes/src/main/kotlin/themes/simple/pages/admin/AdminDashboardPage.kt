package dev.yidafu.blog.themes.simple.pages.admin

import dev.yidafu.blog.themes.*
import kotlinx.html.*

fun FlowContent.statisticItem(
  label: String,
  value: String,
) {
  div {
    classes = setOf("stat-card", "shadow")
    div {
      classes = setOf("stat-card__label")
      +label
    }
    div {
      classes = setOf("stat-card__value")
      +value
    }
  }
}

class AdminDashboardPage(modal: DataModal) : AdminPage(modal) {
  override fun DIV.layoutBlock() {
    val articleCount = modal.long("articleCount", 0L)
    val accessCount = modal.long("accessCount", 0L)
    div {
      classes = setOf("flex-fill", "flex-row", "flex-wrap", "bg-zinc-100", "p-8")
      statisticItem("文章数", articleCount.toString())
      statisticItem("访问总数", accessCount.toString())
    }
  }
}

class AdminDashboardPageProvider : CacheablePageProvider() {
  override fun getName(): String = PageNames.ADMIN_DASHBOARD

  override fun createPage(modal: DataModal): Page {
    return AdminDashboardPage(modal)
  }
}
