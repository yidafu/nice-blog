package dev.yidafu.blog.admin.views.pages

import dev.yidafu.blog.admin.views.layouts.AdminLayout
import dev.yidafu.blog.common.view.tpl.PageTemplate
import dev.yidafu.blog.common.vo.PageVO
import kotlinx.html.h1

class PicturePage(override val vo: PageVO) : PageTemplate<PageVO>() {
  override fun render(): String {
    return AdminLayout(vo).layout {
      h1 {
        +"Pictures Page"
      }
    }.finalize()
  }
}
