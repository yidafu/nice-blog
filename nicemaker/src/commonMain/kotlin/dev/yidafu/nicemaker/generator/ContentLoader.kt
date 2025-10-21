package dev.yidafu.nicemaker.generator

import dev.yidafu.nicemaker.config.SiteConfig
import dev.yidafu.nicemaker.config.SourceConfig
import dev.yidafu.nicemaker.core.dto.CommonArticleDTO
import dev.yidafu.nicemaker.parser.*
import dev.yidafu.nicemaker.platform.process.ProcessUtils
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem

private val logger = KotlinLogging.logger {}

/**
 * 内容加载器
 * 负责从 Git 仓库加载所有文章内容
 */
class ContentLoader(
  private val config: SiteConfig,
) {
  suspend fun loadAllArticles(): List<CommonArticleDTO> {
    val sourceInfo = when (val source = config.content.source) {
      is SourceConfig.GitSource -> "Git repository: ${source.url}"
      is SourceConfig.FileSource -> "Local directory: ${source.path}"
    }
    logger.info { "Loading content from: $sourceInfo" }

    // 1. 克隆或拉取仓库
    val repoPath = cloneOrPullRepository()

    // 2. 创建处理器
    val processors = createProcessors()

    // 3. 扫描和处理文件
    val articles = mutableListOf<CommonArticleDTO>()

    val files = findAllFiles(repoPath)

    files.forEach { path ->
      // 跳过 AboutMe.md（单独处理）
      if (path.name == "AboutMe.md") {
        return@forEach
      }

      processors.find { it.filter(path) }?.let { processor ->
        try {
          val article = processor.transform(path)
          articles.add(article)
          logger.info { "✓ Processed: ${path.name}" }
        } catch (e: Throwable) {
          logger.error(e) { "✗ Failed to process: ${path.name}" }
          // 飞书文件解析失败时给出友好提示
          if (path.name.endsWith(".feishu.yml")) {
            logger.warn { "⚠️  Feishu parser may have compatibility issues. Skipping ${path.name}" }
          }
        }
      }
    }

    logger.info { "Loaded ${articles.size} articles" }
    return articles
  }

  /**
   * 递归查找所有文件
   */
  private fun findAllFiles(rootPath: Path): List<Path> {
    val files = mutableListOf<Path>()
    val excludeDirs = setOf(".git", ".venv", ".ipynb_checkpoints")

    fun scanDirectory(dirPath: Path) {
      if (!SystemFileSystem.exists(dirPath)) return
      if (!SystemFileSystem.metadataOrNull(dirPath)?.isDirectory!!) return

      val pathString = dirPath.toString()
      if (excludeDirs.any { pathString.contains(it) }) return

      SystemFileSystem.list(dirPath).forEach { childPath ->
        val metadata = SystemFileSystem.metadataOrNull(childPath)
        when {
          metadata == null -> {}
          metadata.isRegularFile -> files.add(childPath)
          metadata.isDirectory -> scanDirectory(childPath)
        }
      }
    }

    scanDirectory(rootPath)
    return files
  }

  private suspend fun cloneOrPullRepository(): Path {
    return when (val source = config.content.source) {
      is SourceConfig.GitSource -> {
        val localPath = Path(source.localPath)
        if (!SystemFileSystem.exists(localPath)) {
          logger.info { "Cloning repository..." }
          cloneRepository(source, localPath)
        } else {
          logger.info { "Pulling latest changes..." }
          pullRepository(source, localPath)
        }
        localPath
      }
      is SourceConfig.FileSource -> {
        val path = Path(source.path)
        logger.info { "Using local directory: ${source.path}" }
        if (!SystemFileSystem.exists(path)) {
          throw RuntimeException("Local directory not found: ${source.path}")
        }
        path
      }
    }
  }

  private suspend fun cloneRepository(source: SourceConfig.GitSource, target: Path) {
    // 创建父目录
    target.parent?.let { parent ->
      if (!SystemFileSystem.exists(parent)) {
        SystemFileSystem.createDirectories(parent)
      }
    }

    val result = ProcessUtils.executeGitCommand(
      "git",
      "clone",
      "-b",
      source.branch,
      source.url,
      target.toString(),
      workingDir = null
    )

    logger.debug { result.output.joinToString("\n") }

    if (result.resultCode != 0) {
      throw RuntimeException("Failed to clone repository: ${result.output.joinToString("\n")}")
    }
  }

  private suspend fun pullRepository(source: SourceConfig.GitSource, repoDir: Path) {
    val result = ProcessUtils.executeGitCommand(
      "git",
      "pull",
      "origin",
      source.branch,
      workingDir = repoDir
    )

    logger.debug { result.output.joinToString("\n") }

    if (result.resultCode != 0) {
      logger.warn { "Failed to pull repository, using existing content: ${result.output.joinToString("\n")}" }
    }
  }

  private fun createProcessors(): List<Parser> {
    // 创建简化的 ArticleManager（不依赖数据库）
    val articleManager = StaticArticleManager(config)

    // 所有解析器（全平台通用）
    val parsers = mutableListOf<Parser>(
      dev.yidafu.nicemaker.parser.markdown.MarkdownParser(articleManager),
      dev.yidafu.nicemaker.parser.notebook.NotebookParser(articleManager),
    )

    // 添加 Feishu 解析器（如果配置了）
    val feishuAppId = config.content.feishu?.appId
    val feishuAppSecret = config.content.feishu?.appSecret
    if (feishuAppId != null && feishuAppSecret != null &&
        feishuAppId.isNotBlank() && feishuAppSecret.isNotBlank()) {
      try {
        parsers.add(dev.yidafu.nicemaker.parser.feishu.FeishuParser(
          articleManager,
          feishuAppId,
          feishuAppSecret
        ))
        logger.info { "[ContentLoader] ✓ Feishu parser enabled" }
      } catch (e: Exception) {
        logger.warn(e) { "[ContentLoader] Failed to load FeishuParser" }
      }
    }

    return parsers
  }

  suspend fun loadAboutMe(): String {
    logger.info { "Loading AboutMe.md..." }

    val localPath = when (val source = config.content.source) {
      is SourceConfig.GitSource -> Path(source.localPath)
      is SourceConfig.FileSource -> Path(source.path)
    }
    val aboutMePath = Path(localPath.toString() + "/AboutMe.md")

    if (!SystemFileSystem.exists(aboutMePath)) {
      logger.warn { "AboutMe.md not found in repository" }
      return ""
    }

    try {
      // 使用 MarkdownParser 转换
      val articleManager = StaticArticleManager(config)
      val markdownProcessor = dev.yidafu.nicemaker.parser.markdown.MarkdownParser(articleManager)

      val dto = markdownProcessor.transform(aboutMePath)

      logger.info { "✓ AboutMe.md loaded successfully" }
      return dto.html
    } catch (e: Exception) {
      logger.error(e) { "Failed to convert AboutMe.md to HTML" }
      return "<p>加载关于我页面失败</p>"
    }
  }
}

