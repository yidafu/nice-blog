package dev.yidafu.blog.themes.simple.pages.admin

import dev.yidafu.blog.themes.*
import io.github.allangomes.kotlinwind.css.*
import kotlinx.html.DIV
import kotlinx.html.FlowContent
import kotlinx.html.div
import kotlinx.html.style

fun FlowContent.statisticItem(label: String, value: String) {
  div("shadow") {
    style = kw.inline { padding[3]; width[75]; margin[4]; background.white }

    div {
      style = kw.inline { text.neutral[I300]; font.size[4]; height[6];flex.items_center.justify_start }
      +label
    }
    div {
      style = kw.inline { text.neutral[I950]; font.size[8]; height[10]; flex.items_center.justify_start }
      +value
    }
  }
}

class AdminDashboardPage(modal: DataModal) : AdminPage(modal) {
  override fun DIV.layoutBlock() {
    val vo = modal.dashboardData
    div {
      style = kw.inline { flex.fill.row.wrap; background.zinc[I100]; padding[8] }
      statisticItem("文章数", vo.articleCount.toString())
      statisticItem("访问总数", vo.accessCount.toString())
    }
  }
}

class AdminDashboardPageProvider : CacheablePageProvider() {

  override fun getName(): String = PageNames.ADMIN_DASHBOARD

  override fun createPage(modal: DataModal): Page {
    return AdminDashboardPage(modal)
  }
}
