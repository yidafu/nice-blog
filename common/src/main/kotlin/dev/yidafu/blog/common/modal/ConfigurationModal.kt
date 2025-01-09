package dev.yidafu.blog.common.modal

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalTime


@Entity
@Table(name = "b_configuration", uniqueConstraints = [
  UniqueConstraint(columnNames = ["configKey"])
])
data class ConfigurationModal(
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
  val configKey: String = "",
  @Column
  val configValue: String = "",
) // : BaseModel()
