package dev.yidafu.blog.admin.views.pages.article

import dev.yidafu.blog.common.ext.formatString
import dev.yidafu.blog.common.vo.ArticleVO
import dev.yidafu.blog.i18n.AdminTxt
import io.github.allangomes.kotlinwind.css.I100
import io.github.allangomes.kotlinwind.css.I50
import io.github.allangomes.kotlinwind.css.XL2
import io.github.allangomes.kotlinwind.css.kw
import kotlinx.html.DIV
import kotlinx.html.FlowContent
import kotlinx.html.div
import kotlinx.html.style

fun FlowContent.DescriptionItem(
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

class ArticleDetailPage(override val vo: ArticleVO) : ArticleDetailBasePage(vo) {
  override fun getContent(): DIV.() -> Unit =
    {
      div {
        style = kw.inline { flex.row.wrap }
        DescriptionItem(AdminTxt.column_id.toString(vo.locale), vo.id.toString())
        DescriptionItem(AdminTxt.column_identifier.toString(vo.locale), vo.identifier ?: "-")
        DescriptionItem(AdminTxt.column_title.toString(vo.locale), vo.title)
        DescriptionItem(AdminTxt.column_status.toString(vo.locale), (vo.status ?: 0).toString())
        DescriptionItem(AdminTxt.column_cover.toString(vo.locale), vo.cover ?: "-")
        DescriptionItem(AdminTxt.column_updated_at.toString(vo.locale), vo.updatedAt.formatString())
      }
    }
}
