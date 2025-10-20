package dev.yidafu.nicemaker.cli

import dev.yidafu.nicemaker.theme.TemplateManagerLoader
import dev.yidafu.nicemaker.generator.SiteConfig
import dev.yidafu.nicemaker.generator.StaticSiteGenerator
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*
import kotlinx.cli.*
import kotlinx.coroutines.runBlocking
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.writeString
import java.io.File
import java.time.LocalDate
import kotlin.system.exitProcess

private val logger = KotlinLogging.logger {}

/**
 * 动态加载主题（通过反射）
 */
private fun loadThemesViaReflection() {
  val themeClasses = listOf(
    "dev.yidafu.nicemaker.theme.simple.SimpleTemplateManager",
    "dev.yidafu.nicemaker.theme.blank.BlankTemplateManager",
  )

  themeClasses.forEach { className ->
    try {
      val clazz = Class.forName(className)
      val instance = clazz.getDeclaredConstructor().newInstance()
      TemplateManagerLoader.register(instance as dev.yidafu.nicemaker.theme.TemplateManager)
      logger.info { "Loaded theme: $className" }
    } catch (e: Exception) {
      logger.warn { "Failed to load theme: $className - ${e.message}" }
    }
  }
}

fun main(args: Array<String>) {
  // 动态加载主题
  loadThemesViaReflection()

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

fun buildCommand(
  configPath: String,
  verbose: Boolean,
) {
  println("📦 NiceMaker - Static Site Generator")
  println()

  if (verbose) {
    System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "debug")
  }

  val configFile = Path(configPath)

  if (!SystemFileSystem.exists(configFile)) {
    logger.error { "Configuration file not found: $configFile" }
    println("❌ Configuration file not found: $configFile")
    println("   Create 'nice.yaml' in your project root")
    exitProcess(1)
  }

  try {
    logger.info { "Loading configuration from: $configPath" }
    val config = SiteConfig.load(configFile)
    val generator = StaticSiteGenerator(config)
    runBlocking {
      generator.build()
    }

    println()
    println("🎉 Done! Your site is ready.")
    println("   Output: ${config.build.output}")
    println()
    println("Next steps:")
    println("  - Run 'maker serve' to preview")
    println("  - Deploy the '${config.build.output}' directory to your host")
  } catch (e: Exception) {
    logger.error(e) { "Build failed" }
    println("❌ Build failed: ${e.message}")
    if (System.getProperty("org.slf4j.simpleLogger.defaultLogLevel") == "debug") {
      e.printStackTrace()
    }
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

fun newCommand(
  title: String,
  type: String,
) {
  logger.info { "Creating new $type article: $title" }

  val filename =
    title.lowercase()
      .replace(Regex("[^a-z0-9\\s]"), "")
      .replace(Regex("\\s+"), "-")

  val date = LocalDate.now()

  val (content, extension) =
    when (type) {
      "markdown" -> {
        val template =
          """
          ---
          title: $title
          cover:
          description:
          ---

          # $title

          Your content here...

          """.trimIndent()
        template to "md"
      }
      "feishu" -> {
        val template =
          """
          # 必填：飞书文档 ID
          docxId: "YOUR_DOCX_ID_HERE"

          # 可选：文章标题
          title: "$title"

          # 可选：封面图片
          cover: ""

          # 可选：文章标签
          tags:
            -

          # 可选：系列名称
          series: ""

          # 可选：文章摘要
          summary: ""
          """.trimIndent()
        template to "feishu.yml"
      }
      else -> throw IllegalArgumentException("Unknown type: $type")
    }

  val file = Path("content/$date-$filename.$extension")
  file.parent?.let { parent ->
    if (!SystemFileSystem.exists(parent)) {
      SystemFileSystem.createDirectories(parent)
    }
  }
  SystemFileSystem.sink(file).buffered().use { sink ->
    sink.writeString(content)
  }

  logger.info { "Article created: $file" }
  println("✅ Created: $file")
}

fun printHelp() {
  println(
    """
    📦 NiceMaker - Static Site Generator

    Usage:
      maker <command> [options]

    Commands:
      build [options]       Generate static site
      serve [options]       Start preview server
      new <title> [options] Create new article

    Examples:
      maker build
      maker build -c my-config.yaml -v
      maker serve -p 8080
      maker new "My First Post"
      maker new "Feishu Doc" -t feishu

    Learn more: https://github.com/yidafu/nicemaker
    """.trimIndent(),
  )
}

