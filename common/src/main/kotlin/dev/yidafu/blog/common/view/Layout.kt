package dev.yidafu.blog.common.view

import kotlinx.html.DIV
import kotlinx.html.TagConsumer

interface Layout {
  fun layout(block: DIV.() -> Unit) : TagConsumer<String>

}
