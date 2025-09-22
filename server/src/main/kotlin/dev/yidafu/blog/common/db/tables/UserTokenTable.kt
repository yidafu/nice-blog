package dev.yidafu.blog.common.db.tables

import org.jetbrains.exposed.v1.datetime.datetime

/**
 * 用户令牌表定义
 * 对应 UserTokenModal 实体类
 */
object UserTokenTable : BaseTable("b_user_token") {
  val name = varchar("name", 255).default("(hidden)")
  val token = varchar("token", 32).uniqueIndex().nullable()
  val description = varchar("description", 255).nullable()
  val createdBy = varchar("created_by", 255).nullable()
  val expiresAt = datetime("expires_at").nullable()
}
