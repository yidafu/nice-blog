package dev.yidafu.nicemaker.themes.simple.pages.front

import dev.yidafu.nicemaker.common.vo.TagVO
import dev.yidafu.nicemaker.themes.CacheablePageProvider
import dev.yidafu.nicemaker.themes.DataModal
import dev.yidafu.nicemaker.themes.Page
import dev.yidafu.nicemaker.themes.PageNames
import kotlinx.html.*

class TagListPage(modal: DataModal) : FrontPage(modal) {
  private val tags: List<TagVO> = modal.list<TagVO>("tags")

  override fun MAIN.createContent() {
    div {
      classes = setOf("container")

      h1 {
        classes = setOf("page-title")
        +"标签"
      }

      if (tags.isEmpty()) {
        p {
          classes = setOf("empty-message")
          +"暂无标签"
        }
      } else {
        div {
          classes = setOf("tag-list")

          tags.forEach { tag ->
            a {
              href = "/articles?tag=${tag.slug}"
              classes = setOf("tag-item")

              span {
                classes = setOf("tag-name")
                +tag.name
              }
              span {
                classes = setOf("tag-count")
                +"(${tag.count})"
              }
            }
          }
        }
      }
    }
  }
}

class TagListPageProvider : CacheablePageProvider() {
  override fun getName(): String = PageNames.TAG_LIST

  override fun createPage(modal: DataModal): Page {
    return TagListPage(modal)
  }
}

