package dev.yidafu.blog.admin.jobs

import dev.yidafu.blog.common.dto.MarkdownArticleDTO
import dev.yidafu.blog.dev.yidafu.blog.engine.GitSynchronousTask
import dev.yidafu.blog.dev.yidafu.blog.engine.SyncContext
import java.io.File
import java.net.URI


class NiceGitSynchronousTask(
  ctx: SyncContext,
  private val delegate: DatabaseDelegate,
) : GitSynchronousTask(ctx) {
  override  fun updateImage(img: File): URI {
    ctx.log("upload image: ${img.path}")
    val url = delegate.uploadFile(img)
    return URI(url)
  }

  override fun persistentPost(dto: MarkdownArticleDTO) {
    delegate.updateArticle(dto)
  }

  interface DatabaseDelegate {
    fun uploadFile(file: File): String
    fun updateArticle(dto: MarkdownArticleDTO): Boolean
  }
}
