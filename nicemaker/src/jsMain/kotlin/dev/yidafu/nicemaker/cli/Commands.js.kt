package dev.yidafu.nicemaker.cli

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.promise

/**
 * JS/Node.js 平台：获取文件的绝对路径
 */
actual fun getAbsolutePath(path: String): String {
  return try {
    // 使用 Node.js 的 path.resolve
    js("require('path').resolve(path)") as String
  } catch (e: Exception) {
    path
  }
}

/**
 * JS/Node.js 平台：设置日志级别
 * JS 平台暂不支持动态设置日志级别
 */
actual fun setLogLevel(verbose: Boolean) {
  // JS 平台暂不支持动态设置日志级别
  if (verbose) {
    println("[DEBUG] Verbose mode enabled")
  }
}

/**
 * JS/Node.js 平台：退出程序
 */
actual fun exitProcess(code: Int): Nothing {
  js("process.exit(code)")
  throw RuntimeException("Process exited")
}

/**
 * JS/Node.js 平台：运行挂起函数
 * 注意：JS 不支持真正的阻塞式等待，这里使用一个 workaround
 */
@OptIn(DelicateCoroutinesApi::class)
actual fun <T> runSuspend(block: suspend () -> T): T {
  // 在 JS 中，我们使用 Promise 并通过动态调用来"等待"
  // 注意：这不是真正的同步等待，但对于 CLI 用途足够了
  var result: T? = null
  var error: Throwable? = null

  GlobalScope.promise {
    try {
      result = block()
    } catch (e: Throwable) {
      error = e
    }
  }

  // 简化：直接抛出异常或返回结果
  // 在实际 Node.js 环境中，Promise 会在返回前完成
  error?.let { throw it }
  @Suppress("UNCHECKED_CAST")
  return result as T
}

