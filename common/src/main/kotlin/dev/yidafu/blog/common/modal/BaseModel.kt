package dev.yidafu.blog.common.modal

import jakarta.persistence.*
import java.time.LocalDateTime

@MappedSuperclass
open class BaseModel {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  var id: Long? = null

  @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
  var createdAt: LocalDateTime? = null

  @Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
  var updatedAt: LocalDateTime? = null
}
