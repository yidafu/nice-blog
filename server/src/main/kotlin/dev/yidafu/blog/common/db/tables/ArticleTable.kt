package dev.yidafu.blog.common.db.tables

import dev.yidafu.blog.common.modal.ArticleSourceType
import dev.yidafu.blog.common.modal.ArticleStatus

/**
 * 文章表定义
 * 对应 ArticleModel 实体类
 */
object ArticleTable : BaseTable("b_article") {
  val title = varchar("title", 255)
  val cover = varchar("cover", 255).nullable()
  val identifier = varchar("identifier", 255).nullable()
  val series = varchar("series", 255).nullable()
  val status = enumeration<ArticleStatus>("status").nullable()
  val summary = varchar("summary", 1024).nullable()
  val content = text("content").nullable() // 原始内容
  val html = text("html").nullable() // 渲染后的HTML

  // 枚举类型的处理
  val sourceType = enumeration("source_type", ArticleSourceType::class)

  val hash = varchar("hash", 64).nullable() // MD5哈希值
}
