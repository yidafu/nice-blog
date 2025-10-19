package dev.yidafu.nicemaker.common.vo

import kotlinx.serialization.Serializable

@Serializable
data class SeriesVO(
  val id: String,
  val name: String,
  val description: String = "",
  val articleCount: Int,
  val articles: List<ArticleVO> = emptyList(),
)

