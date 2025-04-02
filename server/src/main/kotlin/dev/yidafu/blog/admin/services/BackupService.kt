package dev.yidafu.blog.admin.services

import dev.yidafu.blog.common.services.BaseService
import org.jooq.CloseableDSLContext
import org.jooq.Record
import org.jooq.Table
import org.jooq.impl.DSL
import org.koin.core.annotation.Single

@Single
class BackupService(
  context: CloseableDSLContext,
) : BaseService(context) {
  val batchSize = 1000

  suspend fun createBackup() =
    runDB { context ->
      val exportSql = mutableListOf<String>()

      val sqliteMasterTable: Table<Record> = DSL.table("sqlite_master").asTable("s")

      // SELECT sql FROM sqlite_master WHERE type IN ('table', 'index', 'trigger', 'view')
//    val sqlField: Field<String>? = sqliteMasterTable.field("sql", String::class.java);
//    val typeField: Field<String>? = sqliteMasterTable.field("type", String::class.java);
      val sqlList =
        context
          .selectFrom(sqliteMasterTable)
          .where("type IN ('table', 'index', 'trigger', 'view')").fetch()
      sqlList.forEach {
        val sql = it.get("sql", String::class.java)
        val name = it.get("type", String::class.java)
        if (sql != null) {
          if (name == "table") {
            exportSql.add("DROP TABLE IF EXISTS $name;")
          }
          exportSql.add("$sql;")
          exportSql.add("\n")
        }
      }

      for (table in context.meta().tables) {
        val count = context.fetchCount(table)
        (0..(count / batchSize)).forEach { index ->

          val result = context.selectFrom(table).limit(index * batchSize, batchSize).fetch()
          for (record in result) {
            exportSql.add(buildInsertSql(table, record))
          }
          exportSql.add("")
        }
      }

//    File("./temp.sql").writeText(exportSql.joinToString("\n"))
    }

  private fun buildInsertSql(
    table: Table<*>,
    record: Record,
  ): String {
    val fields = record.fields()

    return "INSERT INTO ${table.name} (${fields.joinToString(",\n") { f -> f.name }}) VALUES (${
      fields.joinToString(",\n") { f ->
        when (val value = record.get(f)) {
          null -> {
            "NULL"
          }

          is Number -> {
            "$value"
          }

          else -> {
            // 转义 \n, "
            "\"${value.toString().replace("\n", "\\n").replace("\"", "\\\"")}\""
          }
        }
      }
    });"
  }
}
