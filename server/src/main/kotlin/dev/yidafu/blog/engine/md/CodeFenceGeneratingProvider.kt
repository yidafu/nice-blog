package dev.yidafu.blog.engine.md

import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.getTextInNode
import org.intellij.markdown.html.GeneratingProvider
import org.intellij.markdown.html.HtmlGenerator

class CodeFenceGeneratingProvider : GeneratingProvider {

  override fun processNode(
    visitor: HtmlGenerator.HtmlGeneratingVisitor,
    text: String,
    node: ASTNode,
  ) {
    val indentBefore = node.getTextInNode(text).commonPrefixWith(" ".repeat(10)).length

    visitor.consumeHtml("<pre>")

    var state = 0

    var childrenToConsider = node.children
    if (childrenToConsider.last().type == MarkdownTokenTypes.CODE_FENCE_END) {
      childrenToConsider = childrenToConsider.subList(0, childrenToConsider.size - 1)
    }

    var lastChildWasContent = false

    val attributes = ArrayList<String>()
    var language = ""
    for (child in childrenToConsider) {
      if (state == 1 && child.type == MarkdownTokenTypes.CODE_FENCE_CONTENT) {
        val codeStr = HtmlGenerator.trimIndents(child.getTextInNode(text), indentBefore)
        visitor.consumeHtml(CustomCodeHighlight.generateCodeHighlight(codeStr.toString(), language))
        lastChildWasContent = child.type == MarkdownTokenTypes.CODE_FENCE_CONTENT
      }

      if (state == 0 && child.type == MarkdownTokenTypes.FENCE_LANG) {
        language = HtmlGenerator.leafText(text, child).toString().trim().split(' ')[0]
        attributes.add("class=\"language-${language}\"")
      }
      if (state == 0 && child.type == MarkdownTokenTypes.EOL) {
        visitor.consumeTagOpen(node, "code", *attributes.toTypedArray())
        state = 1
      }
    }
    if (state == 0) {
      visitor.consumeTagOpen(node, "code", *attributes.toTypedArray())
    }
    if (lastChildWasContent) {
      visitor.consumeHtml("\n")
    }
    visitor.consumeHtml("</code></pre>")
  }
}
