package dev.yidafu.blog.common.dto

import java.time.LocalTime

data class MarkdownArticleDTO(
  val filename: String,
  val series: String,
  val frontMatter: FrontMatterDTO?,
  val rawContext: String,
  val html: String,
  val createTime: LocalTime,
  val updateTime: LocalTime,
)
