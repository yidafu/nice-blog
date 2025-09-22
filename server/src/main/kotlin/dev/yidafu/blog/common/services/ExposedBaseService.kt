package dev.yidafu.blog.common.services

import dev.yidafu.blog.common.db.ExposedDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

/**
 * 基于Exposed的基础服务类
 */
abstract class ExposedBaseService {
  /**
   * 在协程环境中执行数据库操作
   */
  protected suspend fun <T> runDB(block: suspend () -> T): T {
    return withContext(Dispatchers.IO) {
      block()
    }
  }

  /**
   * 在非协程环境中执行数据库操作（阻塞式）
   */
  protected fun <T> runDBBlocking(block: () -> T): T {
    return transaction {
      block()
    }
  }
  /**
   * 初始化数据库连接
   */
  internal fun initDb() {
    ExposedDatabase.init()
    ExposedDatabase.createTables()
  }
}
