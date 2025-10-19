package dev.yidafu.nicemaker.themes

import kotlinx.html.HTML

interface Page {
  val modal: DataModal

  fun render(html: HTML)
}
