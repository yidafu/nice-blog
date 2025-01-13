package dev.yidafu.blog.common.modal

import jakarta.persistence.*
import java.sql.Date

@Entity
@Table(name = "b_user_token")
class UserTokenModal(
  @Column
  var name: String = "(hidden)",
  @Column(unique = true, nullable = false, length = 32)
  var token: String,
  @Column
  var description: String? = null,
  @Column(nullable = false, name = "created_by")
  var createdBy: Date,
  @Column(nullable = false, name = "expires_at")
  var expiresAt: Date,
) : BaseModel()
