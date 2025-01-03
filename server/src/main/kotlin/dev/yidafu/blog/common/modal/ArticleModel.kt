package dev.yidafu.blog.common.modal

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "b_article")
class ArticleModel(
  @Column
  val title: String = "",
  @Column
  var cover: String? = null,
  @Column
  var identifier: String? = null,
  @Column
  var seriesId: Long? = null,
  @Column
  var status: Int? = null,
  @Column
  var summary: String? = null,
  @Column
  var content: String? = null
) : BaseModel()
