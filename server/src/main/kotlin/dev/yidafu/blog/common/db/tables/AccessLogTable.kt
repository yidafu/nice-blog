package dev.yidafu.blog.common.db.tables

import org.jetbrains.exposed.v1.datetime.datetime

/**
 * 访问日志表定义
 * 对应 AccessLogModal 实体类
 */
object AccessLogTable : BaseTable("b_access_log") {
  val uid = varchar("uid", 16).nullable()
  val accessTime = datetime("access_time").nullable()
  val sourceUrl = varchar("source_url", 512).nullable()
  val referrerUrl = varchar("referrer_url", 512).nullable()
  val ip = varchar("ip", 64).nullable()
  val ua = varchar("ua", 64).nullable()
}
