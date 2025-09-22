package dev.yidafu.blog.common.db.tables

/**
 * 配置表定义
 * 对应 ConfigurationModal 实体类
 */
object ConfigurationTable : BaseTable("b_configuration") {
  val configKey = varchar("config_key", 255).uniqueIndex()
  val configValue = varchar("config_value", 4096)
}
