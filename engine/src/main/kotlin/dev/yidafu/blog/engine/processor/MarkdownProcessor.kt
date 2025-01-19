package dev.yidafu.blog.dev.yidafu.blog.engine.processor

import com.charleskorn.kaml.Yaml
import dev.yidafu.blog.common.dto.FrontMatterDTO
import dev.yidafu.blog.common.dto.CommonArticleDTO
import dev.yidafu.blog.common.modal.ArticleSourceType
import dev.yidafu.blog.dev.yidafu.blog.engine.CodeFenceGeneratingProvider
import dev.yidafu.blog.dev.yidafu.blog.engine.findChildrenOfType
import dev.yidafu.blog.dev.yidafu.blog.engine.indexOf
import dev.yidafu.blog.dev.yidafu.blog.engine.slice
import kotlinx.serialization.decodeFromString
import org.intellij.markdown.IElementType
import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.CompositeASTNode
import org.intellij.markdown.ast.findChildOfType
import org.intellij.markdown.ast.getTextInNode
import org.intellij.markdown.ast.visitors.RecursiveVisitor
import org.intellij.markdown.flavours.gfm.GFMFlavourDescriptor
import org.intellij.markdown.html.GeneratingProvider
import org.intellij.markdown.html.HtmlGenerator
import org.intellij.markdown.parser.LinkMap
import org.intellij.markdown.parser.MarkdownParser
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.attribute.BasicFileAttributes
import java.time.LocalDateTime
import java.time.ZoneId
import kotlin.io.path.extension
import kotlin.io.path.name
import kotlin.io.path.nameWithoutExtension

data class MLink(val url: String, val alt: String)

class GFMFlavorExtendDescriptor : GFMFlavourDescriptor() {
  override fun createHtmlGeneratingProviders(
    linkMap: LinkMap,
    baseURI: org.intellij.markdown.html.URI?,
  ): Map<IElementType, GeneratingProvider> {
    return super.createHtmlGeneratingProviders(linkMap, baseURI) +
      hashMapOf(
        MarkdownElementTypes.CODE_FENCE to CodeFenceGeneratingProvider(),
      )
  }
}

class MarkdownProcessor : IProcessor {

  private val flavour = GFMFlavorExtendDescriptor()
  private val parser = MarkdownParser(flavour)

  override fun filter(path: Path): Boolean {
    return path.extension == "md" &&
      path.nameWithoutExtension != "README"
  }

  override fun transform(path: Path): CommonArticleDTO {
    val markdownFile = path.toFile()
    val text = markdownFile.readText()
    val filename = path.name
    val attrs = Files.readAttributes(path, BasicFileAttributes::class.java)

    val createDate = LocalDateTime.ofInstant(attrs.creationTime().toInstant(), ZoneId.systemDefault())
    val updateDate = LocalDateTime.ofInstant(attrs.lastModifiedTime().toInstant(), ZoneId.systemDefault())

    val frontMatterDTO = parseFrontMatter(markdownFile, text, parser.buildMarkdownTreeFromString(text))

    val textWithoutFrontMatter =
      frontMatterDTO?.rawContent?.let { rawContent ->
        text.replace(rawContent, "")
      } ?: text

    val tree = parser.buildMarkdownTreeFromString(textWithoutFrontMatter)
    MarkdownVisitor(path.toFile(), textWithoutFrontMatter).visitNode(tree)
    val html = HtmlGenerator(textWithoutFrontMatter, tree, flavour).generateHtml()

    val dto =
      CommonArticleDTO(
        filename,
        "",
        frontMatterDTO,
        textWithoutFrontMatter,
        html,
        createDate,
        updateDate,
        ArticleSourceType.Markdown
      )
    return dto
  }

  private fun parseFrontMatter(
    markdownFile: File,
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

            val dto = Yaml.default.decodeFromString<FrontMatterDTO>(frontMatterText.toString())

            dto.rawContent = text.substring(horizontalRules[0].startOffset, secondHorizontalRule.endOffset)
          }
        }
      }
    }
    return null
  }

  inner class MarkdownVisitor(
    private val markdownFile: File,
    private val markdownText: String,
  ) : RecursiveVisitor() {
    override fun visitNode(node: ASTNode) {
      if (node is CompositeASTNode) {
        if (node.type == MarkdownElementTypes.IMAGE) {
          extractImageLinks(node)
        }
      }
      super.visitNode(node)
    }

    private fun extractImageLinks(node: ASTNode) {
      node.findChildOfType(MarkdownElementTypes.INLINE_LINK)?.let { linKNode ->
        val linkUrl = linKNode.findChildOfType(MarkdownElementTypes.LINK_DESTINATION)?.getTextInNode(markdownText)
        val label = linKNode.findChildOfType(MarkdownElementTypes.LINK_TEXT)?.getTextInNode(markdownText)
        val link = MLink(linkUrl.toString(), label.toString())
//        log.info("m link => {}", link)

//        val remoteUrl = updateImage(resolvePath(markdownFile, linkUrl.toString()))
      }
    }
  }
}
