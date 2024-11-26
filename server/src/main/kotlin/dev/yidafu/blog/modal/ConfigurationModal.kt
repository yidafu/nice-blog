package dev.yidafu.blog.modal

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint


@Entity
@Table(name = "b_configuration", uniqueConstraints = [
  UniqueConstraint(columnNames = ["configKey"])
])
data class ConfigurationModal(
  @Column
  val configKey: String = "",
  @Column
  val configValue: String = "",
) : BaseModel() {
}
