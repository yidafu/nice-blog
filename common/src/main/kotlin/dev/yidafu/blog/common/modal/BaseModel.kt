package dev.yidafu.blog.common.modal

import jakarta.persistence.*
import kotlinx.datetime.LocalDateTime

@MappedSuperclass
open class BaseModel {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  var id: Int? = null

  @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
  var createdAt: LocalDateTime? = null

  @Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
  var updatedAt: LocalDateTime? = null
}
