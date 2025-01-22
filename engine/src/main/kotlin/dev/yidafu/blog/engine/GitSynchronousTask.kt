package dev.yidafu.blog.dev.yidafu.blog.engine

import com.github.syari.kgit.KGit
import dev.yidafu.blog.common.dto.CommonArticleDTO
import kotlinx.coroutines.*
import org.eclipse.jgit.lib.TextProgressMonitor
import java.io.File
import java.io.Writer
import java.net.URI
import kotlin.math.log

class LogWriter(val logger: Logger, val uuid: String) : Writer() {
//  private val logger: Logger by inject(Logger::class.java)

  override fun close() {
  }

  override fun flush() {
  }

  override fun write(
    cbuf: CharArray,
    off: Int,
    len: Int,
  ) {
  }

  @OptIn(DelicateCoroutinesApi::class)
  override fun write(str: String) {
    GlobalScope.launch {
      logger.log(uuid, str)
    }
  }
}

open class GitSynchronousTask(
  config: GitConfig,
  olistener: SynchronousListener,
  logger: Logger,
  articleManager: ArticleManager,
) : BaseGitSynchronousTask(config, olistener, logger, articleManager) {
  override suspend fun updateImage(img: File): URI {
    logger.log(taskId, "upload image: ${img.path}")
    val url = articleManager.processImage(img)
    return url
  }

  override suspend fun persistentPost(dto: CommonArticleDTO) {
    articleManager.saveArticle(dto)
  }

  override suspend fun fetchRepository(): File {
    val monitor = TextProgressMonitor(LogWriter(logger, taskId))
    val directory = gitConfig.getLocalRepoFile()
//    return directory
    val git =
      if (!directory.exists()) {
        logger.log(taskId, "repository is not exist, clone repository into ${directory.toPath()}")
        KGit.cloneRepository {
          setURI(gitUrl)
          setTimeout(60)
          setDirectory(directory)
          setProgressMonitor(monitor)
        }
      } else {
        logger.log(taskId, "open local repository in ${directory.absolutePath}")
        KGit.open(directory)
      }

    val branch = gitBranch
    logger.log(taskId, "pull origin $branch")
    val res =
      git.pull {
        remoteBranchName = gitConfig.branch
        setProgressMonitor(monitor)
      }
    logger.log(taskId, "pull result $res")
    // close Git resource
    git.close()
    return directory
  }

  override fun cleanup() {
  }
}
