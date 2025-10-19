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
import org.slf4j.LoggerFactory
import java.sql.Connection

/**
 * Exposed数据库配置类
 */
object ExposedDatabase {
  private val logger = LoggerFactory.getLogger(ExposedDatabase::class.java)

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

  /**
   * 执行初始化 SQL 文件
   */
  fun executeSqlFile(sqlFilePath: String) {
    try {
      // 从 resources 读取 SQL 文件
      val sqlContent =
        this::class.java.classLoader.getResourceAsStream(sqlFilePath)?.bufferedReader()?.readText()
          ?: run {
            logger.warn("SQL file not found: $sqlFilePath")
            return
          }

      logger.info("Executing SQL file: $sqlFilePath")

      // 在事务中执行 SQL
      transaction {
        // 分割 SQL 语句（以分号分隔）
        val statements = sqlContent.split(";").map { it.trim() }.filter { it.isNotBlank() }

        statements.forEach { sql ->
          try {
            // 执行每条 SQL 语句
            exec(sql)
            logger.debug("Executed SQL: ${sql.take(100)}...")
          } catch (e: Exception) {
            logger.error("Error executing SQL: ${sql.take(100)}...", e)
            // 继续执行其他语句
          }
        }
      }

      logger.info("SQL file executed successfully: $sqlFilePath")
    } catch (e: Exception) {
      logger.error("Error reading or executing SQL file: $sqlFilePath", e)
    }
  }
}
