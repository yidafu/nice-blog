package dev.yidafu.blog.themes.simple.pages.admin.article

import dev.yidafu.blog.common.ext.formatString
import dev.yidafu.blog.i18n.AdminTxt
import dev.yidafu.blog.themes.*
import io.github.allangomes.kotlinwind.css.I100
import io.github.allangomes.kotlinwind.css.I50
import io.github.allangomes.kotlinwind.css.XL2
import io.github.allangomes.kotlinwind.css.kw
import kotlinx.html.DIV
import kotlinx.html.FlowContent
import kotlinx.html.div
import kotlinx.html.style
fun FlowContent.descriptionItem(
  title: String,
  content: String,
) {
  div {
    style =
      kw.inline {
        flex.row
        width[60]
        border.all[1].gray[I100]
      }
    div {
      style =
        kw.inline {
          flex.items_center
          font.bold.size[XL2]
          background.gray[I50]
          width[20]
          padding[2]
          font.size[6]
        }
      +title
    }
    div {
      style =
        kw.inline {
          width[40]
          padding[2]
          flex.items_center
          font.size[4]
        }
      +content
    }
  }
}

class AdminArticleDetailPage(modal: DataModal) : AdminArticlePage(modal) {
  override fun DIV.createContent() {
    val vo = modal.articleDetail
    div {
      style = kw.inline { flex.row.wrap }
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
