package dev.yidafu.blog.dev.yidafu.blog.engine

import dev.yidafu.blog.common.dto.MarkdownArticleDTO
import org.slf4j.LoggerFactory
import java.io.File
import java.net.URI

class LocalSynchronousTask(context: SyncContext) : GitSynchronousTaskTemplate(context) {
  override fun fetchRepository(): File {
    return File("/Users/dovyih/Codes/nice-blog/example-blog")
  }


  override fun updateImage(img: File): URI {
    ctx.log("upload image ${img.path}")
    return img.toURI()
  }

  override fun persistentPost(dto: MarkdownArticleDTO) {
    ctx.log("persistent post ==> ${dto.filename}")
  }

  override fun cleanup() {
  }
}
