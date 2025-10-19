package dev.yidafu.blog.themes

import kotlinx.html.HTML

interface Page {
  val modal: DataModal

  fun render(html: HTML)
}
