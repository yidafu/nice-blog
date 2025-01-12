package dev.yidafu.blog.common.modal

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalTime


@Entity
@Table(name = "b_configuration", uniqueConstraints = [
  UniqueConstraint(columnNames = ["config_key"])
])
data class ConfigurationModal(
  @Column(name = "config_key")
  var configKey: String = "",

  @Column(name = "config_value")
  var configValue: String = "",
)  : BaseModel()
