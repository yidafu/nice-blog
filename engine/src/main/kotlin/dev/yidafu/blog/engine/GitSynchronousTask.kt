package dev.yidafu.blog.dev.yidafu.blog.engine

import com.github.syari.kgit.KGit
import dev.yidafu.blog.common.dto.CommonArticleDTO
import kotlinx.coroutines.*
import org.eclipse.jgit.lib.TextProgressMonitor
import org.koin.java.KoinJavaComponent.inject
import java.io.File
import java.io.Writer
import java.net.URI

class LogWriter() : Writer() {
  private val logger: Logger by inject(Logger::class.java)

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
      logger.log(str)
    }
  }
}

open class GitSynchronousTask(
  config: GitConfig,
  listener: SynchronousListener,

  ) : GitSynchronousTaskTemplate(config, listener) {

  private val articleService: ArticleManager by inject(ArticleManager::class.java)
  override suspend fun updateImage(img: File): URI {
    logger.log("upload image: ${img.path}")
    val url = articleService.processImage(img)
    return url
  }

  override suspend fun persistentPost(dto: CommonArticleDTO) {
    articleService.saveArticle(dto)
  }
  override suspend fun fetchRepository(): File {
    val monitor = TextProgressMonitor(LogWriter())
    val directory = gitConfig.getLocalRepoFile()
    val git =
      if (!directory.exists()) {
        logger.log("repository is not exist, clone repository into ${directory.toPath()}")
        KGit.cloneRepository {
          setURI(gitConfig.url)
          setTimeout(60)
          setDirectory(directory)
          setProgressMonitor(monitor)
        }
      } else {
        logger.log("open local repository in ${directory.absolutePath}")
        KGit.open(directory)
      }

    val branch = gitConfig.branch.ifEmpty { "main" }
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
