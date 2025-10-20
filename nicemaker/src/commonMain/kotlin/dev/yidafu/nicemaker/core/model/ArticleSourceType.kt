package dev.yidafu.nicemaker.core.model

import kotlinx.serialization.Serializable

@Serializable
enum class ArticleSourceType {
  Markdown,
  Notebook,
  Feishu,
}



