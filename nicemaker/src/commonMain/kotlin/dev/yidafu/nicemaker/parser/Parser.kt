package dev.yidafu.nicemaker.parser

import dev.yidafu.nicemaker.core.dto.CommonArticleDTO
import kotlinx.io.files.Path

/**
 * 内容解析器接口
 * 负责将不同格式的文件（Markdown, Notebook等）解析为文章对象
 */
interface Parser {
  /**
   * 判断是否可以处理该文件
   */
  fun filter(path: Path): Boolean

  /**
   * 将文件解析为文章对象
   */
  suspend fun transform(path: Path): CommonArticleDTO
}
