package dev.yidafu.blog.common.db.tables

/**
 * 用户表定义
 * 对应 UserModal 实体类
 */
object UserTable : BaseTable("b_user") {
  val username = varchar("username", 255).uniqueIndex()
  val password = varchar("password", 255)
  val email = varchar("email", 255).nullable()
  val status = integer("status").nullable()
  val loginCount = varchar("login_count", 255).nullable()
}
