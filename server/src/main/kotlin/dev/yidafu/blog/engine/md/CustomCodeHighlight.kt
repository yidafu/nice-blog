package dev.yidafu.blog.engine.md

import dev.snipme.highlights.Highlights
import dev.snipme.highlights.model.*
import kotlinx.html.*
import kotlinx.html.stream.createHTML

object CustomCodeHighlight {
  private fun formatCode(code: String): String {
    return code.replace("\n", "<br/>\n")
      .replace(" ", "&nbsp;")
      .replace("\t", "&nbsp;&nbsp;")
  }

  private fun SPAN.appendCode(code: String) {
    if (code.isNotEmpty()) {
      unsafe {
        +formatCode(code)
      }
    }
  }

  private fun TagConsumer<String>.appendCode(
    code: String,
    color: Int = Int.MIN_VALUE,
  ) {
    if (code.isNotEmpty()) {
      span {
        if (color != Int.MIN_VALUE) {
          style = "color: #${Integer.toHexString(color)}"
        }
        appendCode(code)
      }
    }
  }

  private fun toSyntaxLanguage(language: String): SyntaxLanguage =
    when (language.toLowerCase()) {
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

  class TreeNode(
    val data: CodeHighlight,
    val children: List<TreeNode>,
  ) {
    fun getChildStart(): Int {
      return children.minOfOrNull { it.data.location.start } ?: data.location.start
    }

    fun getChildEnd(): Int {
      return children.maxOfOrNull { it.data.location.end } ?: data.location.end
    }

    fun getSortedChildren(): List<TreeNode> {
      return children.sortedBy { it.data.location.start }
    }
  }

  private fun buildTree(highlights: List<CodeHighlight>): List<TreeNode> {
    if (highlights.isEmpty()) return emptyList()
    // 需要过滤的元素列表
    val children = mutableListOf<CodeHighlight>()
    val newList =
      highlights.map { h ->
        // 找到每个code highlight区间的元素
        val list =
          highlights.filter { h2 ->
            h2 != h && h.inRange(h2)
          }
        children.addAll(list)

        val subChildren = buildTree(list)
        TreeNode(h, subChildren)
      }
    // 过滤子节点
    return newList.filterNot { h ->
      children.contains(h.data)
    }
  }

  internal fun generateCodeHighlight(
    code: String,
    language: String,
  ): String {
    val highlights =
      Highlights.Builder().code(code)
        .theme(SyntaxThemes.atom())
        .language(toSyntaxLanguage(language))
        .build()

    /**
     * 将 code highlight 构建成一个树
     *            1~10
     *          /    \
     *         1~8   9~10
     *       /    \
     *     1~4   5~8
     * 再按照深度优先遍历即可
     */
    val list = buildTree(highlights.getHighlights())

    val sortedHighlights = list.sortedBy { it.data.location.start }
    val codeHtml =
      createHTML().apply {
        if (sortedHighlights.isEmpty()) return@apply

        var lastHighlight = sortedHighlights.first().data

        fun buildNode(nodes: List<TreeNode>) {
          nodes.forEach { node ->
            val highlight = node.data
            if (lastHighlight.location.end < highlight.location.end) {
              val codeSegment = code.substring(lastHighlight.location.end, highlight.location.start)
              appendCode(codeSegment)
            }
            lastHighlight = highlight

            if (highlight is ColorHighlight) {
              if (node.children.isEmpty()) {
                val codeSegment = code.substring(highlight.location.start, highlight.location.end)
                appendCode(codeSegment, highlight.rgb)
              } else {
                span {
                  style = "color: #${Integer.toHexString(highlight.rgb)}"
                  appendCode(
                    code.substring(highlight.location.start, node.getChildStart()),
                  )
                  buildNode(node.getSortedChildren())
                  appendCode(
                    code.substring(node.getChildEnd(), highlight.location.end),
                  )
                }
              }
            } else if (highlight is BoldHighlight) {
              b {
                if (node.children.isEmpty()) {
                  appendCode(code.substring(highlight.location.start, highlight.location.end))
                } else {
                  appendCode((code.substring(highlight.location.start, node.getChildStart())))
                  buildNode(node.getSortedChildren())
                  appendCode(
                    code.substring(node.getChildEnd(), highlight.location.end),
                  )
                }
              }
            }
          }
        }

        buildNode(sortedHighlights)

        val last = sortedHighlights.last()
        if (last.data.location.end < code.length) {
          span { appendCode(code.substring(last.data.location.end)) }
        }
      }.finalize()

    return codeHtml
  }
}

inline fun CodeHighlight.inRange(other: CodeHighlight): Boolean {
  return location.start <= other.location.start && other.location.end <= location.end
}
