package dev.yidafu.nicemaker.common.modal

import kotlinx.serialization.Serializable

@Serializable
enum class ArticleSourceType {
  Markdown,
  Notebook,
  Feishu,
}


