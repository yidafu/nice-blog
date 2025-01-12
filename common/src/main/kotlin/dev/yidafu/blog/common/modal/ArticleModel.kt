package dev.yidafu.blog.common.modal

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalTime


@Entity
@Table(name = "b_article")
class ArticleModel(
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  var id: Long? = null,

  @CreationTimestamp
  @Column(name = "created_at")
  var createdAt: LocalTime? = null,

  @UpdateTimestamp
  @Column(name = "updated_at")
  var updatedAt: LocalTime? = null,
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
  @Column(columnDefinition = "text")
  var content: String? = null,
  @Column(columnDefinition = "text")
  var html: String? = null,
) // : BaseModel()


