package dev.yidafu.blog.common.sse

import dev.yidafu.blog.engine.md.CustomCodeHighlight

data class SseModel(
  val event: String? = null,
  val data: String = "",
  val id: String? = null,
  val retry: Number? = null,
) {
  override fun toString(): String {
    val sseStrings = arrayListOf<String>()

    if (event != null) sseStrings.add("event: $event")
    sseStrings.add("data: <p>${highlightLog(data)}</p>")
    if (id != null) sseStrings.add("id: $id")
    if (retry != null) sseStrings.add("retry: $retry")
    sseStrings.add("\n")

    return sseStrings.joinToString(separator = "\n")
  }

  private fun highlightLog(log: String): String {
    return CustomCodeHighlight.generateCodeHighlight(log, "log")
  }
}
