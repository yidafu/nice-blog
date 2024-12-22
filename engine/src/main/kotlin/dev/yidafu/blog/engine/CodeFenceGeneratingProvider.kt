package dev.yidafu.blog.dev.yidafu.blog.engine

import dev.snipme.highlights.Highlights
import dev.snipme.highlights.model.*
import kotlinx.html.*
import kotlinx.html.stream.createHTML
import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.getTextInNode
import org.intellij.markdown.html.GeneratingProvider
import org.intellij.markdown.html.HtmlGenerator
import org.slf4j.LoggerFactory

class CodeFenceGeneratingProvider : GeneratingProvider {
  private val log = LoggerFactory.getLogger(this::class.java)

  internal fun toSyntaxLanguage(language: String): SyntaxLanguage = when (language.toLowerCase()) {
    "js", "javascript" -> SyntaxLanguage.JAVASCRIPT
    "ts", "typescript" -> SyntaxLanguage.TYPESCRIPT
    "c" -> SyntaxLanguage.C
    "cpp", "c++" -> SyntaxLanguage.CPP
    "c#", "csharp" -> SyntaxLanguage.CSHARP
    "dart" -> SyntaxLanguage.DART
    "java" -> SyntaxLanguage.JAVA
    "kt", "kotlin" -> SyntaxLanguage.KOTLIN
    "rs", "rust" -> SyntaxLanguage.RUST
    "perl" -> SyntaxLanguage.PERL
    "py", "python" -> SyntaxLanguage.PYTHON
    "rb", "ruby" -> SyntaxLanguage.RUBY
    "sh", "shell", "bash" -> SyntaxLanguage.SHELL
    "swift" -> SyntaxLanguage.SWIFT
    "go" -> SyntaxLanguage.GO
    "php" -> SyntaxLanguage.PHP
    else -> SyntaxLanguage.DEFAULT
  }

  internal fun generateCodeHighlight(code: String, language: String): String {

    val highlights = Highlights.Builder().code(code)
      .theme(SyntaxThemes.atom())
      .language(toSyntaxLanguage(language))
      .build()

    val sortedHighlights = highlights.getHighlights().sortedBy { it.location.start }

    // 处理两个 highlight 范围重合，前一个 10~20，后一个 14~16
    val resolveConflictHighlights = mutableListOf<CodeHighlight>()
    var idx = 1
    while (idx < sortedHighlights.size) {
      val previous = sortedHighlights[idx - 1]
      val current = sortedHighlights[idx]
      if (previous.location.end > current.location.start) {
        if (previous is ColorHighlight && current is ColorHighlight) {
          resolveConflictHighlights.add(
            ColorHighlight(
              PhraseLocation(previous.location.start, current.location.start),
              previous.rgb,
            )
          )
          resolveConflictHighlights.add(current)
          resolveConflictHighlights.add(
            ColorHighlight(
              PhraseLocation(current.location.end, previous.location.end),
              previous.rgb
            )
          )
        }
        idx += 2
      } else {
        resolveConflictHighlights.add(previous)
      }
      idx++
    }
    resolveConflictHighlights.add(sortedHighlights[sortedHighlights.lastIndex])

    val codeHtml = createHTML().apply {

      resolveConflictHighlights.fold(0) { acc, highlight ->
        if (acc != highlight.location.start) {
          span {
            + code.substring(acc, highlight.location.start)
          }
        }
        if (highlight is ColorHighlight) {
          span {
            style = "color: #${Integer.toHexString(highlight.rgb)}"
            + code.substring(highlight.location.start, highlight.location.end)
          }
        } else if (highlight is BoldHighlight) {
          b {
            + code.substring(highlight.location.start, highlight.location.end)
          }
        }
        highlight.location.end
      }
    }.finalize()

    return codeHtml
  }

  override fun processNode(visitor: HtmlGenerator.HtmlGeneratingVisitor, text: String, node: ASTNode) {
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
        visitor.consumeHtml(generateCodeHighlight(codeStr.toString(), language))
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
