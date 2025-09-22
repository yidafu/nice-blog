package dev.yidafu.blog.common.db

import dev.yidafu.blog.common.db.tables.AccessLogTable
import dev.yidafu.blog.common.db.tables.ArticleHistoryTable
import dev.yidafu.blog.common.db.tables.ArticleTable
import dev.yidafu.blog.common.db.tables.ConfigurationTable
import dev.yidafu.blog.common.db.tables.SyncTaskTable
import dev.yidafu.blog.common.db.tables.UserTable
import dev.yidafu.blog.common.db.tables.UserTokenTable
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.transactions.TransactionManager
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import java.sql.Connection

/**
 * Exposed数据库配置类
 */
object ExposedDatabase {
  /**
   * 初始化数据库连接
   */
  fun init() {
    // 创建H2数据库连接
    val db =
      Database.connect(
        url = "jdbc:h2:./nice-blog.db",
        driver = "org.h2.Driver",
        user = "sa",
        password = "",
      )

    // 设置事务隔离级别
    TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_READ_COMMITTED
  }

  /**
   * 创建所有表
   */
  fun createTables() {
    // 导入并注册所有表定义
    importTables()

    // 使用事务创建表
    transaction {
      // 创建所有表
      SchemaUtils.createMissingTablesAndColumns(
        UserTable,
        ConfigurationTable,
        UserTokenTable,
        AccessLogTable,
        ArticleTable,
        ArticleHistoryTable,
        SyncTaskTable,
      )
    }
  }

  /**
   * 导入表定义
   * 这里使用函数方式导入，避免类未加载导致的问题
   */
  private fun importTables() {
    // 强制加载表定义类
    val tables =
      listOf(
        UserTable,
        ConfigurationTable,
        UserTokenTable,
        AccessLogTable,
        ArticleTable,
        ArticleHistoryTable,
        SyncTaskTable,
      )
    // 确保表被加载
    tables.forEach { table -> table.tableName }
  }
}
