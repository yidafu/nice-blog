package dev.yidafu.blog.dev.yidafu.blog.engine

import com.github.syari.kgit.KGit
import org.eclipse.jgit.lib.TextProgressMonitor
import java.io.File
import java.io.Writer

class LogWriter(val ctx: SyncContext) : Writer() {
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

  override fun write(str: String) {
    ctx.log(str)
  }
}

abstract class GitSynchronousTask(ctx: SyncContext) : GitSynchronousTaskTemplate(ctx) {
  override fun fetchRepository(): File {
    val monitor = TextProgressMonitor(LogWriter(ctx))
    val directory = ctx.gitConfig.getLocalRepoFile()
    val git =
      if (!directory.exists()) {
        ctx.log("repository is not exist, clone repository into ${directory.toPath()}")
        KGit.cloneRepository {
          setURI(ctx.gitConfig.url)
          setTimeout(60)
          setDirectory(directory)
          setProgressMonitor(monitor)
        }
      } else {
        ctx.log("open local repository in ${directory.absolutePath}")
        KGit.open(directory)
      }

    val branch = ctx.gitConfig.branch.ifEmpty { "main" }
    ctx.log("pull origin $branch")
    val res =
      git.pull {
        remoteBranchName = ctx.gitConfig.branch
        setProgressMonitor(monitor)
      }
    ctx.log("pull result $res")
    // close Git resource
    git.close()
    return directory
  }

  override fun cleanup() {
  }
}
