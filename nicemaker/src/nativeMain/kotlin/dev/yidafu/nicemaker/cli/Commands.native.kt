package dev.yidafu.nicemaker.cli

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.toKString
import kotlinx.coroutines.runBlocking
import platform.posix.getenv
import kotlin.system.exitProcess as kotlinExitProcess

/**
 * Native 平台：获取文件的绝对路径
 * 注意：这是一个简化实现，只做基本的路径判断
 */
@OptIn(ExperimentalForeignApi::class)
actual fun getAbsolutePath(path: String): String {
  // 如果已经是绝对路径，直接返回
  if (path.startsWith("/") || path.startsWith("\\") || path.contains(":\\")) {
    return path
  }
  // 对于相对路径，Native 平台暂时直接返回原路径
  // 在实际使用中，kotlinx.io 会自动处理相对路径
  return path
}

/**
 * Native 平台：设置日志级别
 * Native 平台暂不支持动态设置日志级别
 */
actual fun setLogLevel(verbose: Boolean) {
  // Native 平台暂不支持动态设置日志级别
  if (verbose) {
    println("[DEBUG] Verbose mode enabled")
  }
}

/**
 * Native 平台：退出程序
 */
actual fun exitProcess(code: Int): Nothing {
  kotlinExitProcess(code)
}

/**
 * Native 平台：运行挂起函数
 */
actual fun <T> runSuspend(block: suspend () -> T): T {
  return runBlocking { block() }
}

