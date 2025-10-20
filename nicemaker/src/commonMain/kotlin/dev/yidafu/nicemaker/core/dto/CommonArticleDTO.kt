package dev.yidafu.nicemaker.core.dto

import dev.yidafu.nicemaker.core.model.ArticleSourceType
import kotlinx.datetime.LocalDateTime

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
