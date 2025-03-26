
package dev.yidafu.blog.engine

import dev.snipme.highlights.Highlights
import dev.snipme.highlights.model.*
import kotlinx.html.b
import kotlinx.html.span
import kotlinx.html.stream.createHTML
import kotlinx.html.style

object CustomCodeHighlight {
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
              span {
                +code.substring(lastHighlight.location.end, highlight.location.start)
              }
            }
            lastHighlight = highlight

            if (highlight is ColorHighlight) {
              span {
                style = "color: #${Integer.toHexString(highlight.rgb)}"
                if (node.children.isEmpty()) {
                  +code.substring(highlight.location.start, highlight.location.end)
                } else {
                  +code.substring(highlight.location.start, node.getChildStart())
                  buildNode(node.getSortedChildren())
                  +code.substring(node.getChildEnd(), highlight.location.end)
                }
              }
            } else if (highlight is BoldHighlight) {
              b {
                if (node.children.isEmpty()) {
                  +code.substring(highlight.location.start, highlight.location.end)
                } else {
                  +code.substring(highlight.location.start, node.getChildStart())
                  buildNode(node.getSortedChildren())
                  +code.substring(node.getChildEnd(), highlight.location.end)
                }
              }
            }
          }
        }

        buildNode(sortedHighlights)
//        resolveConflictHighlights.fold(0) { acc, highlight ->
//          if (acc != highlight.location.start) {
//
//            span {
//              +code.substring(acc, highlight.location.start)
//            }
//          }
//
//          highlight.location.end
//        }
      }.finalize()

    return codeHtml
  }
}

inline fun CodeHighlight.inRange(other: CodeHighlight): Boolean {
  return location.start <= other.location.start && other.location.end <= location.end
}
