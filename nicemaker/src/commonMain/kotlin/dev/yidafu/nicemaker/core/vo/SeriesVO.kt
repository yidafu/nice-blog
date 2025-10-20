package dev.yidafu.nicemaker.core.vo

import kotlinx.serialization.Serializable
@Serializable
data class SeriesVO(
  val id: String,
  val name: String,
  val description: String = "",
  val articleCount: Int,
  val articles: List<ArticleVO> = emptyList(),
)
