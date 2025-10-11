package dev.yidafu.blog.common.db.tables

import org.jetbrains.exposed.v1.core.dao.id.IntIdTable
import org.jetbrains.exposed.v1.datetime.datetime

/**
 * 基础表定义，包含通用字段
 */
abstract class BaseTable(name: String) : IntIdTable(name) {
  val createdAt = datetime("created_at").nullable()
  val updatedAt = datetime("updated_at").nullable()
}
