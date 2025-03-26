package dev.yidafu.blog.engine

import com.github.syari.kgit.KGit
import dev.yidafu.blog.common.dto.CommonArticleDTO
import dev.yidafu.blog.engine.TaskScope.Companion.NAME
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.eclipse.jgit.lib.TextProgressMonitor
import org.koin.core.annotation.Scope
import org.koin.core.annotation.Scoped
import java.io.File
import java.io.Writer
import java.net.URI

@Scope(name = NAME)
@Scoped
class LogWriter(val logger: Logger) : Writer() {
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

@Scope(name = NAME)
@Scoped
open class GitSynchronousTask(
  config: GitConfig,
  olistener: SynchronousListener,
  articleManager: ArticleManager,
  logger: Logger,
  private val writer: LogWriter,
) : BaseGitSynchronousTask(config, olistener, logger, articleManager) {
  override suspend fun updateImage(img: File): URI {
    logger.log("upload image: ${img.path}")
    val url = articleManager.processImage(img)
    return url
  }

  override suspend fun persistentPost(dto: CommonArticleDTO) {
    articleManager.saveArticle(dto)
  }

  override suspend fun fetchRepository(): File {
//    return File("/Users/yidafu/github/nice-blog/server/yidafu.dev")
    val monitor = TextProgressMonitor(writer)
    val directory = gitConfig.getLocalRepoFile()
    return directory
    val git =
      if (!directory.exists()) {
        logger.log("repository is not exist, clone repository into ${directory.toPath()}")
        KGit.cloneRepository {
          setURI(gitUrl)
          setTimeout(60)
          setDirectory(directory)
          setProgressMonitor(monitor)
        }
      } else {
        logger.log("open local repository in ${directory.absolutePath}")
        KGit.open(directory)
      }

    val branch = gitBranch
    logger.log("pull origin $branch")
    val res =
      git.pull {
        remoteBranchName = gitConfig.branch
        setProgressMonitor(monitor)
      }
    logger.log("pull result $res")
    // close Git resource
    git.close()
    return directory
  }

  override fun cleanup() {
  }
}
