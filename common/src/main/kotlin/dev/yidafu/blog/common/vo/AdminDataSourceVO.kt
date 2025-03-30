package dev.yidafu.blog.common.vo

import kotlinx.serialization.Serializable

@Serializable
class AdminDataSourceVO(
  val sourceType: String,
  val sourceUrl: String,
  val sourceToken: String,
  val sourceBranch: String,
)
