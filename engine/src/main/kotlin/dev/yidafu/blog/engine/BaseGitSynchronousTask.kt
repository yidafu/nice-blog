package dev.yidafu.blog.dev.yidafu.blog.engine

import dev.yidafu.blog.common.dto.CommonArticleDTO
import dev.yidafu.blog.dev.yidafu.blog.engine.processor.IProcessor
import dev.yidafu.blog.dev.yidafu.blog.engine.processor.MarkdownProcessor
import dev.yidafu.blog.dev.yidafu.blog.engine.processor.NotebookProcessor
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.attribute.BasicFileAttributes

abstract class BaseGitSynchronousTask(
  protected val gitConfig: GitConfig,
  protected val listener: SynchronousListener,
  protected val logger: Logger,
  protected val articleManager: ArticleManager,
) {
  protected val gitUrl: String
    get() = gitConfig.url.ifBlank { throw IllegalArgumentException("git url is blank") }
  protected val gitBranch: String
    get() = gitConfig.branch.ifEmpty { "main" }
  protected val taskId: String = gitConfig.uuid

  private val processors: List<IProcessor> =
    listOf(
      NotebookProcessor(articleManager, logger),
      MarkdownProcessor(articleManager, logger),
    )

  /**
   * clone or update local repository
   */
  abstract suspend fun fetchRepository(): File

  /**
   * upload local image to server
   */
  abstract suspend fun updateImage(img: File): java.net.URI

  /**
   * persistent articles to db/cache etc.
   */
  abstract suspend fun persistentPost(dto: CommonArticleDTO)

  abstract fun cleanup()

  suspend fun sync() {
    // execute sync task in io thread
    listener.onStart()
    try {
      logger.log("start sync task...")
      val repoDirectory = fetchRepository()
      logger.log("scan markdown/notebook in ${repoDirectory.toPath()}")
      val regularFiles =
        Files.find(repoDirectory.toPath(), Int.MAX_VALUE, { path, file: BasicFileAttributes ->
          file.isRegularFile &&
            !path.contains(Path.of(".git")) &&
            !path.contains(Path.of(".venv")) &&
            !path.contains(Path.of(".ipynb_checkpoints"))
        })
      for (path in regularFiles) {
        for (processor in processors) {
          if (processor.filter(path)) {
            val dto = processor.transform(path)
            persistentPost(dto)
          }
        }
      }

      listener.onFinish()
    } catch (e: Exception) {
      logger.log("sync task failed: ${e.message}")
      listener.onFailed(e)
    } finally {
      cleanup()
    }
  }
}
