package dev.yidafu.nicemaker.cli

import dev.yidafu.nicemaker.cli.buildCommand
import dev.yidafu.nicemaker.cli.newCommand
import dev.yidafu.nicemaker.cli.printHelp
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*
import kotlinx.cli.*
import java.io.File
import kotlin.system.exitProcess

private val logger = KotlinLogging.logger {}

fun main(args: Array<String>) {
  // 触发 themes 模块的加载，自动注册主题
  try {
    Class.forName("dev.yidafu.nicemaker.theme.ThemesInit")
  } catch (e: Exception) {
    logger.warn { "Failed to load themes module: ${e.message}" }
  }

  val parser = ArgParser("maker")

  class Build : Subcommand("build", "Generate static site") {
    val config by option(
      ArgType.String,
      shortName = "c",
      description = "Configuration file path",
    ).default("nice.yaml")

    val verbose by option(
      ArgType.Boolean,
      shortName = "v",
      description = "Enable verbose output",
    ).default(false)

    override fun execute() {
      buildCommand(config, verbose)
    }
  }

  class Serve : Subcommand("serve", "Start preview server") {
    val port by option(
      ArgType.Int,
      shortName = "p",
      description = "Server port",
    ).default(3000)

    val dir by option(
      ArgType.String,
      shortName = "d",
      description = "Directory to serve",
    ).default("output")

    override fun execute() {
      serveCommand(port, dir)
    }
  }

  class New : Subcommand("new", "Create new article") {
    val title by argument(
      ArgType.String,
      description = "Article title",
    )

    val type by option(
      ArgType.String,
      shortName = "t",
      description = "Article type (markdown or feishu)",
    ).default("markdown")

    override fun execute() {
      newCommand(title, type)
    }
  }

  parser.subcommands(Build(), Serve(), New())

  try {
    parser.parse(args)
  } catch (e: Exception) {
    logger.error(e) { "Error parsing arguments" }
    printHelp()
    exitProcess(1)
  }
}

fun serveCommand(
  port: Int,
  outputDir: String,
) {
  logger.info { "Starting preview server on port $port" }

  val dir = File(outputDir)
  if (!dir.exists()) {
    logger.error { "Output directory not found: ${dir.absolutePath}" }
    println("❌ Output directory not found: ${dir.absolutePath}")
    println("   Run 'maker build' first")
    exitProcess(1)
  }

  println("🌐 Starting preview server...")
  println("   Serving: $outputDir")
  println("   URL: http://localhost:$port")
  println()
  println("Press Ctrl+C to stop")
  println()

  embeddedServer(CIO, port = port) {
    routing {
      staticFiles("/", dir) {
        default("index.html")
      }
    }
  }.start(wait = true)
}

