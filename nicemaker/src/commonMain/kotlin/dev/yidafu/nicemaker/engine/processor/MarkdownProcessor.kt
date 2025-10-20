package dev.yidafu.nicemaker.engine.processor

import com.charleskorn.kaml.Yaml
import dev.yidafu.nicemaker.common.dto.CommonArticleDTO
import dev.yidafu.nicemaker.common.dto.FrontMatterDTO
import dev.yidafu.nicemaker.common.modal.ArticleSourceType
import dev.yidafu.nicemaker.engine.*
import dev.yidafu.nicemaker.engine.ext.findChildrenOfType
import dev.yidafu.nicemaker.engine.ext.indexOf
import dev.yidafu.nicemaker.engine.ext.slice
import dev.yidafu.nicemaker.engine.md.CodeFenceGeneratingProvider
import dev.yidafu.nicemaker.engine.md.ImageGeneratingProvider
import kotlinx.datetime.LocalDateTime
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.readString
import org.intellij.markdown.IElementType
import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.CompositeASTNode
import org.intellij.markdown.ast.getTextInNode
import org.intellij.markdown.flavours.gfm.GFMFlavourDescriptor
import org.intellij.markdown.html.GeneratingProvider
import org.intellij.markdown.html.HtmlGenerator
import org.intellij.markdown.parser.LinkMap
import org.intellij.markdown.parser.MarkdownParser

data class MLink(val url: String, val alt: String)

class GFMFlavorExtendDescriptor(
  private val articleManager: ArticleManager,
  private val logger: Logger,
  private val mdFile: Path,
) : GFMFlavourDescriptor() {
  override fun createHtmlGeneratingProviders(
    linkMap: LinkMap,
    baseURI: org.intellij.markdown.html.URI?,
  ): Map<IElementType, GeneratingProvider> {
    return super.createHtmlGeneratingProviders(linkMap, baseURI) +
      hashMapOf(
        MarkdownElementTypes.CODE_FENCE to CodeFenceGeneratingProvider(),
        MarkdownElementTypes.IMAGE to
          ImageGeneratingProvider(articleManager, logger) { pathStr: String ->
            Path(mdFile.parent.toString() + "/" + pathStr)
          },
      )
  }
}

class MarkdownProcessor(val articleManager: ArticleManager, private val logger: Logger) : IProcessor {
  override fun filter(path: Path): Boolean {
    val extension = path.name.substringAfterLast('.', "")
    val nameWithoutExtension = path.name.substringBeforeLast('.')
    return extension == "md" && nameWithoutExtension != "README"
  }

  override fun transform(path: Path): CommonArticleDTO {
    logger.logSync("[Markdown] transform markdown $path")
    val text = SystemFileSystem.source(path).buffered().use { it.readString() }
    val filename = path.name
    val metadata = SystemFileSystem.metadataOrNull(path)

    val flavour = GFMFlavorExtendDescriptor(articleManager, logger, path)
    val parser = MarkdownParser(flavour)

    // 使用FileTimeUtils获取文件真实时间戳
    val createDate = dev.yidafu.nicemaker.common.utils.FileTimeUtils.getCreationTime(path)
      ?: LocalDateTime(2024, 1, 1, 0, 0)  // fallback
    val updateDate = dev.yidafu.nicemaker.common.utils.FileTimeUtils.getModifiedTime(path)
      ?: LocalDateTime(2024, 1, 1, 0, 0)  // fallback

    val frontMatterDTO = parseFrontMatter(path, text, parser.buildMarkdownTreeFromString(text))

    val textWithoutFrontMatter =
      frontMatterDTO?.rawContent?.let { rawContent ->
        text.replace(rawContent, "")
      } ?: text
    logger.logSync("[Markdown] markdown front matter $frontMatterDTO")
    val tree = parser.buildMarkdownTreeFromString(textWithoutFrontMatter)
    val html = HtmlGenerator(textWithoutFrontMatter, tree, flavour).generateHtml()

    val dto =
      CommonArticleDTO(
        filename,
        frontMatterDTO?.series ?: "",
        frontMatterDTO,
        textWithoutFrontMatter,
        html,
        createDate,
        updateDate,
        ArticleSourceType.Markdown,
      )
    return dto
  }

  private fun parseFrontMatter(
    markdownFile: Path,
    text: String,
    tree: ASTNode,
  ): FrontMatterDTO? {
    val horizontalRules = tree.findChildrenOfType(MarkdownTokenTypes.HORIZONTAL_RULE)
    if (horizontalRules.size > 1) {
      // markdown file start with `---`
      if (tree.indexOf(horizontalRules[0]) == 0) {
        val secondHorizontalRule = tree.indexOf(MarkdownTokenTypes.HORIZONTAL_RULE, 1)
        val secondHorizontalRuleIdx = tree.indexOf(secondHorizontalRule)
        val frontMatter = tree.slice(1, secondHorizontalRuleIdx)
        frontMatter.forEach { node ->
          if (node is CompositeASTNode) {
            val frontMatterText = node.getTextInNode(text)

            val dto = Yaml.default.decodeFromString(FrontMatterDTO.serializer(), frontMatterText.toString())
            val cover =
              dto.cover.let { cover ->
                articleManager.processImage(Path(markdownFile.parent.toString() + "/" + cover)).toString()
              }
            val rawContent = text.substring(horizontalRules[0].startOffset, secondHorizontalRule.endOffset)

            return dto.copy(cover = cover, rawContent = rawContent)
          }
        }
      }
    }
    return null
  }
}
