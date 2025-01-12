package dev.yidafu.blog.views.layouts

import kotlinx.html.*
import kotlinx.html.dom.createHTMLDocument
import kotlinx.html.dom.document
import kotlinx.html.stream.createHTML

inline fun e(crossinline block: BODY.() -> Unit ) {
  val html = createHTML().apply {
    head {
      title {
        text("Dov Yih")
      }
    }
    body(block = block)
  }.toString()
}
