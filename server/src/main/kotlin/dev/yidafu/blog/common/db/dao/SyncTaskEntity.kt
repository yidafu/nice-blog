package dev.yidafu.blog.common.db.dao

import dev.yidafu.blog.common.db.tables.SyncTaskTable
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass

class SyncTaskEntity(id: EntityID<Int>) : IntEntity(id) {
  companion object : IntEntityClass<SyncTaskEntity>(SyncTaskTable)

  private var entityId by SyncTaskTable.id
  var tId: Int = entityId.value

  var callbackUrl by SyncTaskTable.callbackUrl
  var uuid by SyncTaskTable.uuid
  var status by SyncTaskTable.status
  var logs by SyncTaskTable.logs
  var forceSync by SyncTaskTable.forceSync
  var createdAt by SyncTaskTable.createdAt
  var updatedAt by SyncTaskTable.updatedAt

  override fun toString(): String {
    return "SyncTask(id=$id, uuid=$uuid, status=$status)"
  }
}
