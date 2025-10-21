package dev.yidafu.nicemaker.cli

import dev.yidafu.nicemaker.config.SiteConfig
import dev.yidafu.nicemaker.generator.StaticSiteGenerator
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.writeString

private val logger = KotlinLogging.logger {}

/**
 * 平台特定：获取文件的绝对路径
 */
expect fun getAbsolutePath(path: String): String

/**
 * 平台特定：设置日志级别
 */
expect fun setLogLevel(verbose: Boolean)

/**
 * 平台特定：退出程序
 */
expect fun exitProcess(code: Int): Nothing

/**
 * 平台特定：运行挂起函数
 */
expect fun <T> runSuspend(block: suspend () -> T): T

/**
 * Build 命令 - 生成静态站点
 */
fun buildCommand(
  configPath: String,
  verbose: Boolean,
) {
  println("📦 NiceMaker - Static Site Generator")
  println()

  setLogLevel(verbose)

  // 处理相对路径：转换为绝对路径
  val absoluteConfigPath = getAbsolutePath(configPath)
  val configFile = Path(absoluteConfigPath)

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
    runSuspend {
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
    if (verbose) {
      e.printStackTrace()
    }
    exitProcess(1)
  }
}

/**
 * New 命令 - 创建新文章
 */
fun newCommand(
  title: String,
  type: String,
) {
  logger.info { "Creating new $type article: $title" }

  val filename =
    title.lowercase()
      .replace(Regex("[^a-z0-9\\s]"), "")
      .replace(Regex("\\s+"), "-")

  val date = Clock.System.todayIn(TimeZone.currentSystemDefault())

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
      serve [options]       Start preview server (JVM only)
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

