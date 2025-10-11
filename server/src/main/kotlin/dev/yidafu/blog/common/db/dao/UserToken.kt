package dev.yidafu.blog.common.db.dao

import dev.yidafu.blog.common.db.tables.SyncTaskTable
import dev.yidafu.blog.common.db.tables.UserTokenTable
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass

class UserToken(id: EntityID<Int>) : IntEntity(id) {
  companion object : IntEntityClass<UserToken>(UserTokenTable)

  private var entityId by SyncTaskTable.id
  var tId: Int = entityId.value

  var name by UserTokenTable.name
  var token by UserTokenTable.token
  var description by UserTokenTable.description
  var createdBy by UserTokenTable.createdBy
  var expiresAt by UserTokenTable.expiresAt
  var createdAt by UserTokenTable.createdAt
  var updatedAt by UserTokenTable.updatedAt

  override fun toString(): String {
    return "UserToken(id=$id, name=$name, expiresAt=$expiresAt)"
  }
}
