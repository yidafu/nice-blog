package dev.yidafu.blog.dev.yidafu.blog.engine

import dev.yidafu.blog.common.dto.CommonArticleDTO
import org.koin.java.KoinJavaComponent.inject
import java.io.File
import java.net.URI

class LocalSynchronousTaskBase(
  config: GitConfig,
  listener: SynchronousListener,
  logger: Logger,
  articleManager: ArticleManager,
) : BaseGitSynchronousTask(config, listener, logger, articleManager) {
  private val articleService: ArticleManager by inject(ArticleManager::class.java)

  override suspend fun fetchRepository(): File {
    return File("/Users/dovyih/github/yidafu.dev")
  }

  override suspend fun updateImage(img: File): URI {
    logger.log(taskId, "upload image ${img.path}")
    return img.toURI()
  }

  override suspend fun persistentPost(dto: CommonArticleDTO) {
    logger.log(taskId, "persistent post ==> ${dto.filename}")
    articleService.saveArticle(dto)
  }

  override fun cleanup() {
  }
}
