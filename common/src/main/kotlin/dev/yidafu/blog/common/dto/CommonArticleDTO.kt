package dev.yidafu.blog.common.dto

import dev.yidafu.blog.common.modal.ArticleSourceType
import java.time.LocalDateTime

data class CommonArticleDTO(
  val filename: String,
  val series: String,
  val frontMatter: FrontMatterDTO?,
  val rawContext: String,
  val html: String,
  val createTime: LocalDateTime,
  val updateTime: LocalDateTime,
  val sourceType: ArticleSourceType,
)
