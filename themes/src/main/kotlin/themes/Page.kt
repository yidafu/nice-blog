package dev.yidafu.blog.themes

interface Page {
  val modal: DataModal

  fun createPageHtml(): String
}
