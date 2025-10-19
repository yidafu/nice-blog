package dev.yidafu.nicemaker.generator

import dev.yidafu.nicemaker.common.dto.CommonArticleDTO
import dev.yidafu.nicemaker.engine.processor.*
import io.github.oshai.kotlinlogging.KotlinLogging
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.attribute.BasicFileAttributes

private val logger = KotlinLogging.logger {}

/**
 * 内容加载器
 * 负责从 Git 仓库加载所有文章内容
 */
class ContentLoader(
  private val config: SiteConfig,
) {
  fun loadAllArticles(): List<CommonArticleDTO> {
    logger.info { "Loading content from: ${config.content.source.url}" }

    // 1. 克隆或拉取仓库
    val repoDir = cloneOrPullRepository()

    // 2. 创建处理器
    val processors = createProcessors()

    // 3. 扫描和处理文件
    val articles = mutableListOf<CommonArticleDTO>()

    val files =
      Files.find(
        repoDir.toPath(),
        Int.MAX_VALUE,
        { path, attrs: BasicFileAttributes ->
          attrs.isRegularFile &&
            !path.contains(Path.of(".git")) &&
            !path.contains(Path.of(".venv")) &&
            !path.contains(Path.of(".ipynb_checkpoints"))
        },
      )

    files.forEach { path ->
      // 跳过 AboutMe.md（单独处理）
      if (path.fileName.toString() == "AboutMe.md") {
        return@forEach
      }

      processors.find { it.filter(path) }?.let { processor ->
        try {
          val article = processor.transform(path)
          articles.add(article)
          logger.info { "✓ Processed: ${path.fileName}" }
        } catch (e: Exception) {
          logger.error(e) { "✗ Failed to process: ${path.fileName}" }
        }
      }
    }

    logger.info { "Loaded ${articles.size} articles" }
    return articles
  }

  private fun cloneOrPullRepository(): File {
    val localPath = File(config.content.source.localPath)

    if (!localPath.exists()) {
      logger.info { "Cloning repository..." }
      cloneRepository(localPath)
    } else {
      logger.info { "Pulling latest changes..." }
      pullRepository(localPath)
    }

    return localPath
  }

  private fun cloneRepository(target: File) {
    target.parentFile?.mkdirs()

    val process =
      ProcessBuilder(
        "git",
        "clone",
        "-b",
        config.content.source.branch,
        config.content.source.url,
        target.absolutePath,
      ).redirectErrorStream(true).start()

    process.inputStream.bufferedReader().useLines { lines ->
      lines.forEach { logger.debug { it } }
    }

    val exitCode = process.waitFor()
    if (exitCode != 0) {
      throw RuntimeException("Failed to clone repository")
    }
  }

  private fun pullRepository(repoDir: File) {
    val process =
      ProcessBuilder(
        "git",
        "pull",
        "origin",
        config.content.source.branch,
      ).directory(repoDir).redirectErrorStream(true).start()

    process.inputStream.bufferedReader().useLines { lines ->
      lines.forEach { logger.debug { it } }
    }

    val exitCode = process.waitFor()
    if (exitCode != 0) {
      logger.warn { "Failed to pull repository, using existing content" }
    }
  }

  private fun createProcessors(): List<IProcessor> {
    // 创建简化的 ArticleManager（不依赖数据库）
    val articleManager = StaticArticleManager(config)
    val processorLogger = StaticLogger()

    val processors =
      mutableListOf<IProcessor>(
        MarkdownProcessor(articleManager, processorLogger),
        NotebookProcessor(articleManager, processorLogger),
      )

    // 如果启用飞书
    config.content.feishu?.let { feishuConfig ->
      if (feishuConfig.enabled &&
        feishuConfig.appId.isNotBlank() &&
        feishuConfig.appSecret.isNotBlank()
      ) {
        processors.add(
          FeishuProcessor(
            articleManager,
            processorLogger,
            feishuConfig.appId,
            feishuConfig.appSecret,
          ),
        )
        logger.info { "Feishu processor enabled" }
      }
    }

    return processors
  }

  fun loadAboutMe(): String {
    logger.info { "Loading AboutMe.md..." }

    val localPath = File(config.content.source.localPath)
    val aboutMeFile = File(localPath, "AboutMe.md")

    if (!aboutMeFile.exists()) {
      logger.warn { "AboutMe.md not found in repository" }
      return ""
    }

    try {
      // 使用 MarkdownProcessor 转换
      val articleManager = StaticArticleManager(config)
      val processorLogger = StaticLogger()
      val markdownProcessor = MarkdownProcessor(articleManager, processorLogger)

      val aboutMePath = aboutMeFile.toPath()
      val dto = markdownProcessor.transform(aboutMePath)

      logger.info { "✓ AboutMe.md loaded successfully" }
      return dto.html
    } catch (e: Exception) {
      logger.error(e) { "Failed to convert AboutMe.md to HTML" }
      return "<p>加载关于我页面失败</p>"
    }
  }
}

