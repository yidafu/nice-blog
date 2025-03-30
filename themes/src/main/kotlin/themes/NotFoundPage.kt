package dev.yidafu.blog.themes

import kotlinx.html.h1
import kotlinx.html.stream.createHTML

class NotFoundPage : Page {
  override val modal: DataModal
    get() = DataModal()

  override fun createPageHtml(): String {
    return createHTML().apply {
      h1 {
        +"Not Found"
      }
    }.finalize()
  }
}
