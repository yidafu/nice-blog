package dev.yidafu.blog.admin.services

import dev.yidafu.blog.common.services.BaseService
import dev.yidafu.blog.common.annotation.Service
import org.jetbrains.exposed.v1.jdbc.Database

interface BackupService {
  suspend fun createBackup()
}

@Service
class BackupServiceImpl : BackupService, BaseService() {
  val batchSize = 1000

  override suspend fun createBackup() =
    runDB {
      val exportSql = mutableListOf<String>()

      // 获取数据库连接
      val connection =
        Database.connect("")

//      try {
//        // 查询表结构
//        val metadata = connection.dialectMetadata
//        val rs = metadata.getTables(null, null, "%", arrayOf("TABLE", "VIEW", "INDEX", "TRIGGER"))
//
//        while (rs.next()) {
//          val tableName = rs.getString("TABLE_NAME")
//          val tableType = rs.getString("TABLE_TYPE")
//
//          // 对于表，添加DROP语句
//          if (tableType == "TABLE") {
//            exportSql.add("DROP TABLE IF EXISTS $tableName;")
//          }
//
//          // 获取建表SQL（SQLite特定方法）
//          val stmt = connection.prepareStatement("SELECT sql FROM sqlite_master WHERE type = ? AND name = ?")
//          stmt.setString(1, tableType.lowercase())
//          stmt.setString(2, tableName)
//          val sqlRs = stmt.executeQuery()
//
//          if (sqlRs.next()) {
//            val createSql = sqlRs.getString("sql")
//            if (createSql != null) {
//              exportSql.add("$createSql;")
//              exportSql.add("")
//            }
//          }
//
//          sqlRs.close()
//          stmt.close()
//        }
//
//        rs.close()
//
//        // 获取所有表名
//        val tablesRs = metadata.getTables(null, null, "%", arrayOf("TABLE"))
//        val tables = mutableListOf<String>()
//        while (tablesRs.next()) {
//          tables.add(tablesRs.getString("TABLE_NAME"))
//        }
//        tablesRs.close()
//
//        // 备份每个表的数据
//        for (tableName in tables) {
//          val stmt = connection.prepareStatement("SELECT * FROM $tableName")
//          val dataRs = stmt.executeQuery()
//          val meta = dataRs.metaData
//          val columnCount = meta.columnCount
//
//          while (dataRs.next()) {
//            val fields = mutableListOf<String>()
//            val values = mutableListOf<String>()
//
//            for (i in 1..columnCount) {
//              fields.add(meta.getColumnName(i))
//
//              val value = dataRs.getObject(i)
//              when {
//                value == null -> values.add("NULL")
//                value is Number -> values.add("$value")
//                else -> {
//                  // 转义特殊字符
//                  val escapedValue =
//                    value.toString()
//                      .replace("\n", "\\n")
//                      .replace('"', '"')
//                  values.add("\"$escapedValue\"")
//                }
//              }
//            }
//
//            val insertSql = "INSERT INTO $tableName (${fields.joinToString(", ")}) VALUES (${values.joinToString(", ")});"
//            exportSql.add(insertSql)
//          }
//
//          dataRs.close()
//          stmt.close()
//          exportSql.add("")
//        }
//
//        // 这里可以选择将SQL写入文件
//        // File("./temp.sql").writeText(exportSql.joinToString("\n"))
//      } finally {
//        connection.close()
//      }
//
//      exportSql.joinToString("\n")
    }
}
