package dev.yidafu.nicemaker.parser.notebook

import com.charleskorn.kaml.Yaml
import dev.yidafu.nicemaker.core.dto.CommonArticleDTO
import dev.yidafu.nicemaker.core.dto.FrontMatterDTO
import dev.yidafu.nicemaker.core.model.ArticleSourceType
import dev.yidafu.nicemaker.engine.*
import dev.yidafu.nicemaker.parser.markdown.CustomCodeHighlight
import dev.yidafu.nicemaker.platform.io.FileTimeUtils
import dev.yidafu.nicemaker.parser.Parser
import dev.yidafu.nicemaker.parser.markdown.GFMFlavorExtendDescriptor
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.io.files.Path
import kotlinx.serialization.decodeFromString
import org.intellij.markdown.html.HtmlGenerator
import org.intellij.markdown.parser.MarkdownParser as MD_Parser
import org.jetbrains.jupyter.parser.JupyterParser
import org.jetbrains.jupyter.parser.notebook.*
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.buffered
import kotlinx.io.readString
import kotlinx.io.files.Path as KPath

private val logger = KotlinLogging.logger {}

/**
 * Notebook解析器 - KMP通用实现
 */
class NotebookParser(
  private val articleManager: ArticleManager,
) : Parser {
  private val jsMagic = listOf("%js", "%javascript", "%ts", "%typescript", "%jsx", "%tsx")

  override fun filter(path: KPath): Boolean {
    return path.name.endsWith(".ipynb")
  }

  override suspend fun transform(path: KPath): CommonArticleDTO {
    logger.info { "[Notebook] transform notebook $path" }

    // 使用kotlinx-io读取文件内容
    val notebookContent = SystemFileSystem.source(path).buffered().use { it.readString() }
    val notebook = JupyterParser.parse(notebookContent)

    val flavour = GFMFlavorExtendDescriptor(articleManager, path)
    val parser = MD_Parser(flavour)
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

    logger.info { "[Notebook] notebook front matter $frontMatterDTO" }

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
      rawContext = notebookContent,
      html = html,
      createTime = createDate ?: kotlinx.datetime.LocalDateTime(2024, 1, 1, 0, 0),
      updateTime = updateDate ?: kotlinx.datetime.LocalDateTime(2024, 1, 1, 0, 0),
      sourceType = ArticleSourceType.Notebook,
    )
  }
}

