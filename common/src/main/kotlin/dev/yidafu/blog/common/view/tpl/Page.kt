package dev.yidafu.blog.common.view.tpl

import dev.yidafu.blog.common.vo.PageVO

abstract class Page<T : PageVO>() {
  abstract val vo: PageVO

  abstract fun render(): String
}
