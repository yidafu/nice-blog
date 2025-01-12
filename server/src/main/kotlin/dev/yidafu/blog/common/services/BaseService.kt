package dev.yidafu.blog.common.services

import kotlinx.coroutines.*
import org.jooq.CloseableDSLContext

open class BaseService(private val closeableDSLContext: CloseableDSLContext) {
  private val scope = CoroutineScope(Dispatchers.IO)
  suspend fun<T> runDB(block: (context: CloseableDSLContext) -> T): T = scope.async {
    block(closeableDSLContext)
  }.await()
}
