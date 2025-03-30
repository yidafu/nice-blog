package dev.yidafu.blog.themes.simple.components

import dev.yidafu.blog.themes.icons.Icon
import io.github.allangomes.kotlinwind.css.I50
import io.github.allangomes.kotlinwind.css.I700
import io.github.allangomes.kotlinwind.css.LG
import io.github.allangomes.kotlinwind.css.kw
import kotlinx.html.*

class TABS(initialAttributes: Map<String, String>, consumer: TagConsumer<*>) : DIV(initialAttributes, consumer)

class TabOption(
  val title: String,
  val url: String,
  val icon: Icon,
  val active: Boolean,
)

inline fun FlowContent.tabs(
  classes: String? = null,
  options: List<TabOption>,
  crossinline block: DIV.() -> Unit = {},
) {
  val layout: TABS.() -> Unit = {
    ul {
      style =
        kw.inline {
          flex.auto.grow_0.shrink_0
            .col
          width[50]
        }
      options.forEach { option ->
        tabItem(option)
      }
    }
    div("shadow-lg") {
      style =
        kw.inline {
          margin.left[6]
          padding.y[4].x[8]
          flex.auto
        }
      block()
    }
  }
  return TABS(
    attributesMapOf(
      "class",
      "flex ${classes ?: ""}",
    ),
    consumer,
  ).visit(layout)
}

fun UL.tabItem(option: TabOption) {
  tabItem(option.title, option.url, option.icon, option.active)
}

fun UL.tabItem(
  title: String,
  url: String,
  icon: Icon,
  active: Boolean,
) {
  li("nav-list") {
    a {
      style =
        kw.inline {
          flex.row.items_center
          padding.x[4].y[3]
          border.rounded[LG]
          if (active) {
            text.white
            background.blue[I700]
          } else {
            background.gray[I50]
          }
        }
      href = url
      i {
        style =
          kw.inline {
            width[4].height[4]
            margin.right[2]
          }
        icon.render(this@i)
      }
      +title
    }
  }
}
