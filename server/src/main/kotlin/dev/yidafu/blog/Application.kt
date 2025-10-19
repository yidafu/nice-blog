package dev.yidafu.blog

import dev.yidafu.blog.admin.adminModule
import dev.yidafu.blog.common.TemplateManagerLoader
import dev.yidafu.blog.common.db.ExposedDatabase
import dev.yidafu.blog.fe.frontendModule
import dev.yidafu.blog.generated.injectAllDependencies
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import kotlinx.coroutines.*

// Ktor 应用程序主入口
fun main(args: Array<String>): Unit = runBlocking {
  try {
    println("=== Initializing Nice Blog ===")

    // 统一初始化共享资源（在启动服务器前）
    println("1. Loading template manager...")
    TemplateManagerLoader.load()

    println("2. Initializing database...")
    ExposedDatabase.init()

    println("3. Creating database tables...")
    ExposedDatabase.createTables()

    println("4. Executing setup SQL...")
    ExposedDatabase.executeSqlFile("META-INF/sql/setup.sql")

    println("\n=== Starting Servers ===")

    // 使用 launch 在独立协程中启动服务器
    val adminJob = launch(Dispatchers.Default) {
      println("Starting admin server on port 8080...")
      embeddedServer(CIO, port = 8080, module = Application::adminModule)
        .start(wait = true)
    }

    val frontendJob = launch(Dispatchers.Default) {
      println("Starting frontend server on port 8081...")
      embeddedServer(CIO, port = 8081, module = Application::frontendModule)
        .start(wait = true)
    }

    // 等待一下确保都启动
    delay(2000)
    println("\n✓ Both servers started successfully")
    println("  Admin:    http://localhost:8080")
    println("  Frontend: http://localhost:8081")
    println("  Default admin user: admin / admin123")
    println("\nPress Ctrl+C to stop servers...\n")

    // 等待两个服务器（会一直运行直到手动停止）
    adminJob.join()
    frontendJob.join()
  } catch (e: Exception) {
    println("\n✗ Error starting servers: ${e.message}")
    e.printStackTrace()
  }
}
