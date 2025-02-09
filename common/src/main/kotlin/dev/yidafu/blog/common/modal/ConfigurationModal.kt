package dev.yidafu.blog.common.modal

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint

@Entity
@Table(
  name = "b_configuration",
  uniqueConstraints = [
    UniqueConstraint(columnNames = ["config_key"]),
  ],
)
data class ConfigurationModal(
  @Column(name = "config_key")
  var configKey: String = "",
  @Column(name = "config_value")
  var configValue: String = "",
) : BaseModel()
