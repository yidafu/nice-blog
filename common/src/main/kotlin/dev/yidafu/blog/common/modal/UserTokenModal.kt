package dev.yidafu.blog.common.modal

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import java.time.LocalDateTime

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
  var createdBy: String,
  @Column(nullable = false, name = "expires_at")
  var expiresAt: LocalDateTime,
) : BaseModel()
