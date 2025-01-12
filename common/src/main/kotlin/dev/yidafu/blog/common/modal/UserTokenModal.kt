package dev.yidafu.blog.common.modal

import jakarta.persistence.*
import java.sql.Date

@Entity
@Table(name = "b_user_token")
class UserTokenModal(
  @Column
  val name: String = "(hidden)",
  @Column(unique = true, nullable = false, length = 32)
  val token: String,
  @Column
  val description: String? = null,
  @Column(nullable = false, name = "created_by")
  val createdBy: Date,
  @Column(nullable = false, name = "expires_at")
  val expiresAt: Date,
) : BaseModel()
