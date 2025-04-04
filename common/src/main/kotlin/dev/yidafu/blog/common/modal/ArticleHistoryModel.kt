package dev.yidafu.blog.common.modal

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "b_article_history")
class ArticleHistoryModel(
  /**
   *
   */
  @Column(name = "article_id", comment = "ArticleModel ID")
  val articleId: Long,
  @Column(name = "raw_content", comment = "raw article content")
  val rawContent: String,
  @Column(
    name = "rendered_content",
    comment = "after be rendered(transformed) content",
  )
  val renderedContent: String,
) : BaseModel()
