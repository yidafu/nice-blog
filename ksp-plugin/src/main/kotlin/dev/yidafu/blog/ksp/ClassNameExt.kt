package dev.yidafu.blog.ksp

import com.squareup.kotlinpoet.ClassName

fun ClassName.toVariableName(): String {
  return this.simpleName.replaceFirstChar {
    it.lowercase()
  }
}
