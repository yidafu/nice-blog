package dev.yidafu.blog.themes.simple.pages.admin.article

import dev.yidafu.blog.common.ext.formatString
import dev.yidafu.blog.common.vo.ArticleVO
import dev.yidafu.blog.i18n.AdminTxt
import dev.yidafu.blog.themes.*
import kotlinx.html.*

fun FlowContent.descriptionItem(
  title: String,
  content: String,
) {
  div {
    classes = setOf("desc-item")
    div {
      classes = setOf("desc-item__label")
      +title
    }
    div {
      classes = setOf("desc-item__content")
      +content
    }
  }
}

class AdminArticleDetailPage(modal: DataModal) : AdminArticlePage(modal) {
  override fun DIV.createContent() {
    val vo = modal.obj<ArticleVO>("article") ?: ArticleVO()
    div {
      classes = setOf("flex-row", "flex-wrap")
      descriptionItem(AdminTxt.column_id.toText(), vo.id.toString())
      descriptionItem(AdminTxt.column_identifier.toText(), vo.identifier ?: "-")
      descriptionItem(AdminTxt.column_title.toText(), vo.title)
      descriptionItem(AdminTxt.column_status.toText(), (vo.status ?: 0).toString())
      descriptionItem(AdminTxt.column_cover.toText(), vo.cover ?: "-")
      descriptionItem(AdminTxt.column_updated_at.toText(), vo.updatedAt.formatString())
    }
  }
}

class AdminArticleDetailPageProvider : CacheablePageProvider() {
  override fun getName(): String = PageNames.ADMIN_ARTICLE_DETAIL

  override fun createPage(modal: DataModal): Page = AdminArticleDetailPage(modal)
}
