package dev.yidafu.blog.common.db.dao

import dev.yidafu.blog.common.db.tables.SyncTaskTable
import dev.yidafu.blog.common.db.tables.UserTable
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass

class UserEntity(id: EntityID<Int>) : IntEntity(id) {
  companion object : IntEntityClass<UserEntity>(UserTable)

  private var entityId by SyncTaskTable.id
  var tId: Int = entityId.value
  var username by UserTable.username
  var password by UserTable.password
  var email by UserTable.email
  var status by UserTable.status
  var loginCount by UserTable.loginCount
  var createdAt by UserTable.createdAt
  var updatedAt by UserTable.updatedAt

  override fun toString(): String {
    return "User(id=$id, username=$username, email=$email)"
  }
}
