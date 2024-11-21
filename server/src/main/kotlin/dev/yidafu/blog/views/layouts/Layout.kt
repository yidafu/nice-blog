package dev.yidafu.blog.views.layouts

import kotlinx.html.DIV
import kotlinx.html.TagConsumer

interface Layout {
  fun layout(block: DIV.() -> Unit) : TagConsumer<String>

}
