package dev.yidafu.blog.themes.simple.components

import dev.yidafu.blog.themes.icons.Icon
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
      this.classes = setOf("flex", "flex-col", "w-20")
      options.forEach { option ->
        tabItem(option)
      }
    }
    div {
      this.classes = setOf("shadow-lg", "ml-6", "py-4", "px-8", "flex-fill")
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
      val cssClasses = mutableSetOf("flex-row", "items-center", "px-4", "py-3", "rounded-lg")
      if (active) {
        cssClasses.addAll(listOf("text-white", "bg-blue-700"))
      } else {
        cssClasses.add("bg-gray-50")
      }
      this.classes = cssClasses
      href = url
      i {
        this.classes = setOf("w-4", "h-4", "mr-2")
        icon.render(this@i)
      }
      +title
    }
  }
}
