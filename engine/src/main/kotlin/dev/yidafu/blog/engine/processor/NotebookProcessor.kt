package dev.yidafu.blog.dev.yidafu.blog.engine.processor

import com.charleskorn.kaml.Yaml
import dev.yidafu.blog.common.dto.CommonArticleDTO
import dev.yidafu.blog.common.dto.FrontMatterDTO
import dev.yidafu.blog.common.modal.ArticleSourceType
import dev.yidafu.blog.dev.yidafu.blog.engine.CustomCodeHighlight
import dev.yidafu.blog.dev.yidafu.blog.engine.getGitCreateTime
import dev.yidafu.blog.dev.yidafu.blog.engine.getGitModifyTime
import kotlinx.serialization.decodeFromString
import org.intellij.markdown.html.HtmlGenerator
import org.intellij.markdown.parser.MarkdownParser
import org.jetbrains.jupyter.parser.JupyterParser
import org.jetbrains.jupyter.parser.notebook.*
import java.nio.file.Path
import kotlin.io.path.extension
import kotlin.io.path.name

class NotebookProcessor : IProcessor {
  private val jsMagic = listOf("%js", "%javascript", "%ts", "%typescript", "%jsx", "%tsx")

  private val flavour = GFMFlavorExtendDescriptor()
  private val parser = MarkdownParser(flavour)
//  private val currentDirectory: File by lazy {
//    Paths.get(System.getProperty("user.dir")).toFile()
//  }

  override fun filter(path: Path): Boolean {
    return path.extension == "ipynb"
  }

  /**
   * method 1: base on nbconvert
   * method 2: base on kotlin-jupyter-parser, implement html renderer
   *
   * method 2 is better, because of it lightweight, flexible
   *
   * cons: can't run cell (notebook must be pre-executed) when transform notebook
   */
  override fun transform(path: Path): CommonArticleDTO {
    val file = path.toFile()
    val notebook = JupyterParser.parse(file)

    val list = mutableListOf<String>()
    val cells = notebook.cells
    val frontMatterCell = cells[0]

    val frontMatterDTO = Yaml.default.decodeFromString<FrontMatterDTO>(frontMatterCell.source.replace("---", ""))
    frontMatterDTO.rawContent = frontMatterCell.source

    cells.slice(1..<cells.size).forEach { cell: Cell ->
      when (cell) {
        is MarkdownCell -> {
//          if (cell is MarkdownCell) {
          val tree = parser.buildMarkdownTreeFromString(cell.source)
          val html =
            HtmlGenerator(cell.source, tree, flavour).generateHtml()
              .replace("<body>", "<div class=\"markdown-cell\">")
              .replace("</body>", "</div>")
          list.add(html)
//          }
        }

        is RawCell -> {
          if (cell is RawCell) {
            list.add(
              "<p class=\"raw-cell\">${cell.source}</p>",
            )
          }
        }

        is CodeCell -> {
//          if (cell is CodeCell) {
          val isJS =
            jsMagic.any { magic ->
              cell.source.contains(magic)
            }
          val lang = if (isJS) "typescript" else "kotlin"
          list.add(
            "<code class=\"code-cell language-$lang\"><pre>" +
              CustomCodeHighlight.generateCodeHighlight(cell.source, lang) +
              "</pre></code>",
          )
          list.add("<div class=\"cell-output\">")
          cell.outputs.forEach { output: Output ->
            when (output) {
              is Error -> {
                //  ignore error
              }

              is Stream -> TODO()
              is DisplayData ->
                when (output as DisplayData) {
                  is ExecuteResult -> {
                    output.data["text/plain"]?.let { list.add("<p class=\"plain-text\">$it</p>") }
                    output.data["image/png"]?.let {
                      list.add("<img class=\"image image-png\" src=\"data:image/png;base64,$it\"/>")
                    }
                    output.data["image/jpg"]?.let {
                      list.add("<img class=\"image image-jpg\" src=\"data:image/jpg;base64,$it\"/>")
                    }
                    output.data["text/html"]?.let { list.add(it) }
//                  output.data["application/plot+json"]
                  }
                }
            }
          }
          list.add("</div>")
        }
      }
    }

    val html = list.joinToString("\n")

    return CommonArticleDTO(
      path.name,
      "",
      frontMatterDTO,
      file.readText(),
      html,
      path.getGitCreateTime(),
      path.getGitModifyTime(),
      ArticleSourceType.Notebook,
    )
  }

//  private fun checkOrInstallPackage(pipPackage: String) {
//    // TODO: logging
//    val output1 = "pip show $pipPackage".runCommand(currentDirectory)
//    if (output1.contains("WARNING: Package(s) not found:")) {
//      "pip install $pipPackage".runCommand(currentDirectory)
//    }
//  }
}
