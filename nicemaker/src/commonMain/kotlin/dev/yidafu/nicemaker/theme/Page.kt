package dev.yidafu.nicemaker.theme

import kotlinx.html.HTML

interface Page {
  val modal: DataModal

  fun render(html: HTML)
}
