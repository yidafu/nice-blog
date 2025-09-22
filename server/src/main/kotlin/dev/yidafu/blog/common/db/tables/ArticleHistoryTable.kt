package dev.yidafu.blog.common.db.tables

/**
 * 文章历史表定义
 * 对应 ArticleHistoryModel 实体类
 */
object ArticleHistoryTable : BaseTable("b_article_history") {
  val articleId = integer("article_id").nullable() // 关联的文章ID
  val rawContent = text("raw_content") // 原始文章内容
  val renderedContent = text("rendered_content") // 渲染后的内容
}
