package dev.yidafu.blog.common.db.dao

import dev.yidafu.blog.common.db.tables.ConfigurationTable
import dev.yidafu.blog.common.db.tables.SyncTaskTable
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.IntEntityClass

class ConfigurationEntity(id: EntityID<Int>) : BaseEntity(id) {
  companion object : IntEntityClass<ConfigurationEntity>(ConfigurationTable)

  private var entityId by SyncTaskTable.id
  var tId: Int = entityId.value

  var configKey by ConfigurationTable.configKey
  var configValue by ConfigurationTable.configValue
  var createdAt by ConfigurationTable.createdAt
  var updatedAt by ConfigurationTable.updatedAt

  override fun toString(): String {
    return "Configuration(id=$id, configKey=$configKey, configValue=$configValue)"
  }
}
