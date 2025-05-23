package dev.yidafu.blog.engine

import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.CompositeASTNode
import org.intellij.markdown.ast.findChildOfType
import org.intellij.markdown.ast.getTextInNode
import org.intellij.markdown.html.GeneratingProvider
import org.intellij.markdown.html.HtmlGenerator
import org.slf4j.LoggerFactory
import java.io.File

class ImageGeneratingProvider(
  private val articleManager: ArticleManager,
  private val logger: Logger,
  val resolvePath: (path: String) -> File,
) : GeneratingProvider {
  private val log = LoggerFactory.getLogger(ImageGeneratingProvider::class.java)

  override fun processNode(
    visitor: HtmlGenerator.HtmlGeneratingVisitor,
    text: String,
    node: ASTNode,
  ) {
    if (node is CompositeASTNode) {
      if (node.type == MarkdownElementTypes.IMAGE) {
        node.findChildOfType(MarkdownElementTypes.INLINE_LINK)?.let { linKNode ->
          val linkUrl = linKNode.findChildOfType(MarkdownElementTypes.LINK_DESTINATION)?.getTextInNode(text)
          val label = linKNode.findChildOfType(MarkdownElementTypes.LINK_TEXT)?.getTextInNode(text)
//          val link = MLink(linkUrl.toString(), label.toString())
          logger.logSync("[Markdown] linking node => $label -- $linkUrl")
          val remoteUrl = articleManager.processImage(resolvePath(linkUrl.toString()))
          visitor.consumeHtml("<img src='$remoteUrl' alt='$label' />")
        }
      }
    }
  }
}
