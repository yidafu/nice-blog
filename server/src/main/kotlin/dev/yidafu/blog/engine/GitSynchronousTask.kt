package dev.yidafu.blog.engine

// import com.github.syari.kgit.KGit
import dev.yidafu.blog.common.ConfigurationKeys
import dev.yidafu.blog.common.dto.CommonArticleDTO
import dev.yidafu.blog.common.annotation.Service
import dev.yidafu.blog.common.services.ConfigurationService
import dev.yidafu.blog.engine.processor.FeishuProcessor
import dev.yidafu.blog.engine.processor.IProcessor
import dev.yidafu.blog.engine.processor.MarkdownProcessor
import dev.yidafu.blog.engine.processor.NotebookProcessor
import io.ktor.server.plugins.di.annotations.Named
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
// import org.eclipse.jgit.lib.TextProgressMonitor
import java.io.File
import java.io.Writer
import java.net.URI

class LogWriter(val logger: BaseLogger) : Writer() {
  override fun close() {
  }

  override fun flush() {
  }

  /**
   * intercept by `fun write(str: String)`, so this function never execute
   */
  override fun write(
    cbuf: CharArray,
    off: Int,
    len: Int,
  ) {
  }

  override fun write(str: String) {
    runBlocking {
      CoroutineScope(Dispatchers.IO).async {
        logger.log(str)
      }
    }.onAwait
  }
}

@Service
open class GitSynchronousTask(
  config: GitConfig,
  @Named("dbListener") olistener: SynchronousListener,
  @Named("dbArticle") articleManager: ArticleManager,
  @Named("dbLogger")  logger: BaseLogger,
  private val writer: LogWriter,
  private val configService: ConfigurationService,
) : BaseGitSynchronousTask(
    config,
    olistener,
    logger,
    articleManager,
    createProcessors(articleManager, logger, configService)
  ) {

  companion object {
    private fun createProcessors(
      articleManager: ArticleManager,
      logger: BaseLogger,
      configService: ConfigurationService
    ): List<IProcessor> {
      val processors = mutableListOf<IProcessor>(
        NotebookProcessor(articleManager, logger),
        MarkdownProcessor(articleManager, logger),
      )

      // 如果配置了飞书，添加飞书处理器
      runBlocking {
        try {
          val appId = configService.getByKey(ConfigurationKeys.FEISHU_APP_ID).configValue
          val appSecret = configService.getByKey(ConfigurationKeys.FEISHU_APP_SECRET).configValue

          if (appId.isNotBlank() && appSecret.isNotBlank()) {
            processors.add(FeishuProcessor(articleManager, logger, appId, appSecret))
            logger.log("[Feishu] Feishu processor enabled")
          }
        } catch (e: Exception) {
          logger.log("[Feishu] Failed to initialize Feishu processor: ${e.message}")
        }
      }

      return processors
    }
  }
  override suspend fun updateImage(img: File): URI {
    logger.log("[Image] upload image: ${img.path}")
    val url = articleManager.processImage(img)
    return url
  }

  override suspend fun persistentPost(dto: CommonArticleDTO) {
    articleManager.saveArticle(dto)
  }

  override suspend fun fetchRepository(): File {
    val directory = gitConfig.getLocalRepoFile()
    val branch = gitBranch

    if (!directory.exists()) {
      logger.log("repository is not exist, clone repository into ${directory.toPath()}")
      // 创建父目录
      directory.parentFile?.mkdirs()

      // 使用git命令克隆仓库
      val cloneProcess =
        ProcessBuilder(
          "git",
          "clone",
          gitUrl,
          directory.absolutePath,
        ).redirectErrorStream(true).start()

      // 读取命令输出并记录日志
      cloneProcess.inputStream
        .bufferedReader().useLines {
        it.forEach { line -> logger.log(line) }
      }

      // 等待命令完成
      val exitCode = cloneProcess.waitFor()
      if (exitCode != 0) {
        throw RuntimeException("Failed to clone repository. Exit code: $exitCode")
      }
    } else {
      logger.log("open local repository in ${directory.absolutePath}")

      // 使用git命令拉取更新
      logger.log("pull origin $branch")
      val pullProcess =
        ProcessBuilder(
          "git",
          "pull",
          "origin",
          branch,
        ).directory(directory).redirectErrorStream(true).start()

      // 读取命令输出并记录日志
      val output = StringBuilder()
      pullProcess.inputStream.bufferedReader().useLines {
        it.forEach { line ->
          output.append(line).append("\n")
          logger.log(line)
        }
      }

      // 等待命令完成
      val exitCode = pullProcess.waitFor()
      if (exitCode != 0) {
        throw RuntimeException("Failed to pull repository. Exit code: $exitCode\nOutput: $output")
      }

      logger.log("pull completed successfully")
    }

    return directory
  }

  override fun cleanup() {
  }
}
