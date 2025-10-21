package dev.yidafu.nicemaker.parser.markdown
import dev.yidafu.nicemaker.parser.Parser

import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.YamlConfiguration
import dev.yidafu.nicemaker.core.dto.CommonArticleDTO
import dev.yidafu.nicemaker.core.dto.FrontMatterDTO
import dev.yidafu.nicemaker.core.model.ArticleSourceType
import dev.yidafu.nicemaker.engine.*
import dev.yidafu.nicemaker.util.extension.findChildrenOfType
import dev.yidafu.nicemaker.util.extension.indexOf
import dev.yidafu.nicemaker.util.extension.slice
import dev.yidafu.nicemaker.parser.markdown.CodeFenceGeneratingProvider
import dev.yidafu.nicemaker.parser.markdown.ImageGeneratingProvider
import io.github.oshai.kotlinlogging.KotlinLogging
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

private val logger = KotlinLogging.logger {}

data class MLink(val url: String, val alt: String)

class GFMFlavorExtendDescriptor(
  private val articleManager: ArticleManager,
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
          ImageGeneratingProvider(articleManager) { pathStr: String ->
            Path(mdFile.parent.toString() + "/" + pathStr)
          },
      )
  }
}

class MarkdownParser(val articleManager: ArticleManager) : Parser {
  override fun filter(path: Path): Boolean {
    val extension = path.name.substringAfterLast('.', "")
    val nameWithoutExtension = path.name.substringBeforeLast('.')
    return extension == "md" && nameWithoutExtension != "README"
  }

  override suspend fun transform(path: Path): CommonArticleDTO {
    logger.info { "[Markdown] transform markdown $path" }
    val text = SystemFileSystem.source(path).buffered().use { it.readString() }
    val filename = path.name
    val metadata = SystemFileSystem.metadataOrNull(path)

    val flavour = GFMFlavorExtendDescriptor(articleManager, path)
    val parser = MarkdownParser(flavour)

    // 使用FileTimeUtils获取文件真实时间戳
    val createDate = dev.yidafu.nicemaker.platform.io.FileTimeUtils.getCreationTime(path)
      ?: LocalDateTime(2024, 1, 1, 0, 0)  // fallback
    val updateDate = dev.yidafu.nicemaker.platform.io.FileTimeUtils.getModifiedTime(path)
      ?: LocalDateTime(2024, 1, 1, 0, 0)  // fallback

    val frontMatterDTO = parseFrontMatter(path, text, parser.buildMarkdownTreeFromString(text))

    val textWithoutFrontMatter =
      frontMatterDTO?.rawContent?.let { rawContent ->
        text.replace(rawContent, "")
      } ?: text
    logger.info { "[Markdown] markdown front matter $frontMatterDTO" }
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

        // Extract front matter text directly from the source text
        val frontMatterStart = horizontalRules[0].endOffset
        val frontMatterEnd = secondHorizontalRule.startOffset
        val frontMatterText = text.substring(frontMatterStart, frontMatterEnd).trim()

        val yaml = Yaml(configuration = YamlConfiguration(
          strictMode = false
        ))
        val dto = yaml.decodeFromString(FrontMatterDTO.serializer(), frontMatterText)
        val cover =
          dto.cover.let { cover ->
            articleManager.processImage(Path(markdownFile.parent.toString() + "/" + cover)).toString()
          }
        val rawContent = text.substring(horizontalRules[0].startOffset, secondHorizontalRule.endOffset)

        return dto.copy(cover = cover, rawContent = rawContent)
      }
    }
    return null
  }
}
