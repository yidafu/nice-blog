package dev.yidafu.blog.common.modal

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "b_user")
class UserModal(
  @Column(unique = true)
  var username: String = "",
  @Column
  var password: String = "",
  @Column
  var email: String? = null,
  @Column
  var status: Int? = null,
  @Column
  var loginCount: String? = null,
) : BaseModel()
