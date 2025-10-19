package dev.yidafu.nicemaker.themes

import kotlinx.html.*

class NotFoundPage : Page {
  override val modal: DataModal
    get() = DataModal()

  override fun render(html: HTML) {
    html.apply {
      head {
        title { +"Not Found" }
      }
      body {
        h1 {
          +"Not Found"
        }
      }
    }
  }
}
