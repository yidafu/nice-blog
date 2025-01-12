package dev.yidafu.blog.dev.yidafu.blog.engine

import org.intellij.markdown.IElementType
import org.intellij.markdown.ast.ASTNode

inline fun ASTNode.findChildrenOfType(type: IElementType): List<ASTNode> {
  return children.filter { it.type == type }
}

inline fun ASTNode.indexOf(child: ASTNode): Int {
  return children.indexOf(child)
}

inline fun ASTNode.indexOf(
  type: IElementType,
  offset: Int,
): ASTNode {
  return children.slice(offset..<children.size).first { it.type == type }
}

inline fun ASTNode.slice(
  start: Int,
  end: Int,
): List<ASTNode> {
  return children.slice(start..<end)
}
