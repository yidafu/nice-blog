package dev.yidafu.nicemaker.cli

import kotlinx.coroutines.runBlocking
import java.io.File
import kotlin.system.exitProcess as kotlinExitProcess

/**
 * JVM 平台：获取文件的绝对路径
 */
actual fun getAbsolutePath(path: String): String {
  return File(path).absolutePath
}

/**
 * JVM 平台：设置日志级别
 */
actual fun setLogLevel(verbose: Boolean) {
  if (verbose) {
    System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "debug")
  }
}

/**
 * JVM 平台：退出程序
 */
actual fun exitProcess(code: Int): Nothing {
  kotlinExitProcess(code)
}

/**
 * JVM 平台：运行挂起函数
 */
actual fun <T> runSuspend(block: suspend () -> T): T {
  return runBlocking { block() }
}

