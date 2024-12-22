package dev.yidafu.blog.common.modal

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import java.util.*

@Entity
@Table(name = "b_user")
class UserModal(
  @Column(unique = true)
  val username: String = "",
  @Column
  val password: String = "",
  @Column
  val email: String? = null,
  @Column
  val status: Int? = null,
  @Column
  val loginCount: String? = null,
) : BaseModel()
