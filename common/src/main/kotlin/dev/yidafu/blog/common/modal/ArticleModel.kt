package dev.yidafu.blog.common.modal

import jakarta.persistence.*

/**
 * 考虑两种情况
 * 1. markdown
 * 2. jupyter notebook
 *
 */
@Entity
@Table(name = "b_article")
class ArticleModel(
  @Column
  var title: String = "",

  @Column
  var cover: String? = null,

  @Column
  var identifier: String? = null,


  @Column(name = "series")
  var series: String? = null,


  @Enumerated
  @Column
  var status: ArticleStatus? = null,


  @Column
  var summary: String? = null,

  /**
   * original content
   * markdown is content without front matter
   * notebook is raw content of *.ipynb
   */
  @Column(columnDefinition = "text", comment = "original content")
  var content: String? = null,

  @Column(columnDefinition = "text")
  var html: String? = null,

  @Column
  @Enumerated(value = EnumType.STRING)
  var sourceType: ArticleSourceType,

  @Column(comment = "content's md5 hash for checking weather article update or not")
  var hash: String,
) : BaseModel()


enum class ArticleSourceType {
  Markdown,
  Notebook
}
