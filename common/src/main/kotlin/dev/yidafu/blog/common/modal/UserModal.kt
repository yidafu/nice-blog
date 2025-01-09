package dev.yidafu.blog.common.modal

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalTime
import java.util.*

@Entity
@Table(name = "b_user")
class UserModal(
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  var id: Long? = null,

  @CreationTimestamp
  @Column(name = "created_at")
  var createdAt: LocalTime? = null,

  @UpdateTimestamp
  @Column(name = "updated_at")
  var updatedAt: LocalTime? = null,
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
) // : BaseModel()
