package dev.yidafu.blog.engine.processor

import dev.yidafu.blog.common.dto.CommonArticleDTO
import java.nio.file.Path

interface IProcessor {
  /*
   * weather file need been transformed
   *
   */
  fun filter(path: Path): Boolean

  fun transform(path: Path): CommonArticleDTO
}
