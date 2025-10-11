package dev.yidafu.blog

import dev.yidafu.blog.fe.frontendModule
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*

// Ktor 应用程序主入口
fun main(args: Array<String>) {
  try {
//    embeddedServer(CIO, port = 8080, module = Application::adminModule).start(wait = true)
    embeddedServer(CIO, port = 8081, module = Application::frontendModule).start(wait = true)
  } catch (e: Exception) {
    e.printStackTrace()
//    System.exit(1)
  }
}
