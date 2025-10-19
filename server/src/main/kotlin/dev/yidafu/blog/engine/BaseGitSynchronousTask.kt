package dev.yidafu.blog.engine

import dev.yidafu.blog.common.dto.CommonArticleDTO
import dev.yidafu.blog.engine.processor.IProcessor
import dev.yidafu.blog.engine.processor.MarkdownProcessor
import dev.yidafu.blog.engine.processor.NotebookProcessor
import io.ktor.server.plugins.di.annotations.Named
import java.io.File
import java.net.URI
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.attribute.BasicFileAttributes

interface SynchronousTask {
  suspend fun updateImage(img: File): URI

  /**
   * clone or update local repository
   */
  suspend fun fetchRepository(): File

  /**
   * persistent articles to db/cache etc.
   */
  suspend fun persistentPost(dto: CommonArticleDTO)

  fun cleanup()

  suspend fun sync()
}

abstract class BaseGitSynchronousTask(
  protected val gitConfig: GitConfig,
  @Named("dbListener") protected val listener: SynchronousListener,
  @Named("dbLogger") protected val logger: BaseLogger,
  @Named("dbArticle")  protected val articleManager: ArticleManager,
  protected val processors: List<IProcessor>,
) : SynchronousTask {
  protected val gitUrl: String
    get() = gitConfig.url.ifBlank { throw IllegalArgumentException("git url is blank") }
  protected val gitBranch: String
    get() = gitConfig.branch.ifEmpty { "main" }
  protected val taskId: String = gitConfig.uuid

  override suspend fun sync() {
    // execute sync task in io thread
    listener.onStart()
    try {
      logger.log("[Task] start sync task...")
      val repoDirectory = fetchRepository()
      logger.log("[Task] scan markdown/notebook in ${repoDirectory.toPath()}")
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
      logger.log("[Task] sync task failed: ${e.message}")
      listener.onFailed(e)
    } finally {
      cleanup()
    }
  }
}
