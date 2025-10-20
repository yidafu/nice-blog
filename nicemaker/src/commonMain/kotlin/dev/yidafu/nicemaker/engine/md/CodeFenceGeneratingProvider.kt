package dev.yidafu.nicemaker.engine.md

import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.getTextInNode
import org.intellij.markdown.html.GeneratingProvider
import org.intellij.markdown.html.HtmlGenerator

/**
 * 代码围栏生成提供者 - KMP版本
 * 使用highlights库（1.1.0+支持KMP）进行代码高亮
 */
class CodeFenceGeneratingProvider : GeneratingProvider {
  override fun processNode(
    visitor: HtmlGenerator.HtmlGeneratingVisitor,
    text: String,
    node: ASTNode,
  ) {
    var childrenToConsider = node.children
    if (childrenToConsider.last().type == MarkdownTokenTypes.CODE_FENCE_END) {
      childrenToConsider = childrenToConsider.subList(0, childrenToConsider.size - 1)
    }

    var language = ""
    var codeContent = ""
    for (child in childrenToConsider) {
      if (child.type == MarkdownTokenTypes.FENCE_LANG) {
        language = HtmlGenerator.leafText(text, child).toString().trim().split(' ')[0]
      }
      val ignoreType =
        listOf(
          MarkdownTokenTypes.FENCE_LANG,
          MarkdownTokenTypes.CODE_FENCE_END,
          MarkdownTokenTypes.CODE_FENCE_START,
        )
      if (child.type in ignoreType) {
        continue
      }

      if (child.type == MarkdownTokenTypes.CODE_FENCE_CONTENT) {
        val codeStr = child.getTextInNode(text)

        codeContent += codeStr
      }
      if (child.type == MarkdownTokenTypes.EOL) {
        codeContent += "\n"
      }
    }

    visitor.consumeHtml(
      "<code class=\"code-cell language-$language\"><pre>\n" +
        CustomCodeHighlight.generateCodeHighlight(codeContent, language) +
        "\n</pre></code>",
    )
  }
}
