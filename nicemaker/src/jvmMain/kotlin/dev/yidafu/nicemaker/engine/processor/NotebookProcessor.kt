package dev.yidafu.nicemaker.engine.processor

import com.charleskorn.kaml.Yaml
import dev.yidafu.nicemaker.common.dto.CommonArticleDTO
import dev.yidafu.nicemaker.common.dto.FrontMatterDTO
import dev.yidafu.nicemaker.common.modal.ArticleSourceType
import dev.yidafu.nicemaker.engine.*
import dev.yidafu.nicemaker.engine.md.CustomCodeHighlight
import dev.yidafu.nicemaker.common.utils.FileTimeUtils
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.io.files.Path
import kotlinx.serialization.decodeFromString
import org.intellij.markdown.html.HtmlGenerator
import org.intellij.markdown.parser.MarkdownParser
import org.jetbrains.jupyter.parser.JupyterParser
import org.jetbrains.jupyter.parser.notebook.*
import java.nio.file.Paths
import kotlin.io.path.extension
import kotlin.io.path.name
import kotlinx.io.files.Path as KPath

/**
 * Notebook处理器 - JVM平台专用
 *
 * 依赖：
 * - org.jetbrains.jupyter:kotlin-jupyter-parser (JVM-only)
 * - java.nio.file API
 *
 * 支持处理 .ipynb (Jupyter Notebook) 文件
 */
class NotebookProcessor(
  private val articleManager: ArticleManager,
  private val logger: BaseLogger,
) : IProcessor {
  private val jsMagic = listOf("%js", "%javascript", "%ts", "%typescript", "%jsx", "%tsx")

  override fun filter(path: KPath): Boolean {
    return path.name.endsWith(".ipynb")
  }

  override fun transform(path: KPath): CommonArticleDTO {
    logger.logSync("[Notebook] transform notebook $path")

    val javaPath = Paths.get(path.toString())
    val file = javaPath.toFile()
    val notebook = JupyterParser.parse(file)

    val flavour = GFMFlavorExtendDescriptor(articleManager, logger, path)
    val parser = MarkdownParser(flavour)
    val list = mutableListOf<String>()
    val cells = notebook.cells
    val frontMatterCell = cells[0]

    val dto = Yaml.default.decodeFromString<FrontMatterDTO>(
      frontMatterCell.source.replace("---", "")
    )
    val cover = dto.cover.let {
      val imgPath = Path(path.parent.toString(), it)
      articleManager.processImage(imgPath).toString()
    }
    val frontMatterDTO = dto.copy(cover = cover, rawContent = frontMatterCell.source)

    logger.logSync("[Notebook] notebook front matter $frontMatterDTO")

    cells.slice(1..<cells.size).forEach { cell: Cell ->
      when (cell) {
        is MarkdownCell -> {
          val tree = parser.buildMarkdownTreeFromString(cell.source)
          val html = HtmlGenerator(cell.source, tree, flavour).generateHtml()
            .replace("<body>", "<div class=\"markdown-cell\">")
            .replace("</body>", "</div>")
          list.add(html)
        }

        is RawCell -> {
          list.add("<p class=\"raw-cell\">${cell.source}</p>")
        }

        is CodeCell -> {
          val isJS = jsMagic.any { magic -> cell.source.contains(magic) }
          val lang = if (isJS) "typescript" else "kotlin"
          list.add(
            "<code class=\"code-cell language-$lang\"><pre>" +
              CustomCodeHighlight.generateCodeHighlight(cell.source, lang) +
              "</pre></code>"
          )
          list.add("<div class=\"cell-output\">")
          cell.outputs.forEach { output: Output ->
            when (output) {
              is Error -> { /* ignore error */ }
              is Stream -> { /* TODO */ }
              is DisplayData -> when (output as DisplayData) {
                is ExecuteResult -> {
                  output.data["text/plain"]?.let {
                    list.add("<p class=\"plain-text\">$it</p>")
                  }
                  output.data["image/png"]?.let {
                    list.add("<img class=\"image image-png\" src=\"data:image/png;base64,$it\"/>")
                  }
                  output.data["image/jpg"]?.let {
                    list.add("<img class=\"image image-jpg\" src=\"data:image/jpg;base64,$it\"/>")
                  }
                  output.data["text/html"]?.let { list.add(it) }
                }
              }
            }
          }
          list.add("</div>")
        }
      }
    }

    val html = list.joinToString("\n")

    val createDate = FileTimeUtils.getCreationTime(path)
    val updateDate = FileTimeUtils.getModifiedTime(path)

    return CommonArticleDTO(
      filename = path.name,
      series = "",
      frontMatter = frontMatterDTO,
      rawContext = file.readText(),
      html = html,
      createTime = createDate ?: kotlinx.datetime.LocalDateTime(2024, 1, 1, 0, 0),
      updateTime = updateDate ?: kotlinx.datetime.LocalDateTime(2024, 1, 1, 0, 0),
      sourceType = ArticleSourceType.Notebook,
    )
  }
}

