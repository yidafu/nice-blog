package dev.yidafu.blog.common.db.dao

import dev.yidafu.blog.common.db.tables.AccessLogTable
import dev.yidafu.blog.common.db.tables.SyncTaskTable
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass

class AccessLogEntity(id: EntityID<Int>) : IntEntity(id) {
  companion object : IntEntityClass<AccessLogEntity>(AccessLogTable)

  private var entityId by SyncTaskTable.id
  var tId: Int = entityId.value
  var uid by AccessLogTable.uid
  var accessTime by AccessLogTable.accessTime
  var sourceUrl by AccessLogTable.sourceUrl
  var referrerUrl by AccessLogTable.referrerUrl
  var ip by AccessLogTable.ip
  var ua by AccessLogTable.ua

  override fun toString(): String {
    return "AccessLog(id=$id, uid=$uid, accessTime=$accessTime, sourceUrl=$sourceUrl, referrerUrl=$referrerUrl, ip=$ip, ua=$ua)"
  }
}
