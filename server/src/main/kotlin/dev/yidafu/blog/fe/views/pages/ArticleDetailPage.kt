package dev.yidafu.blog.fe.views.pages

import dev.yidafu.blog.common.view.tpl.Page
import dev.yidafu.blog.common.vo.ArticleVO
import dev.yidafu.blog.fe.views.layouts.FrontendLayout
import dev.yidafu.blog.fe.views.layouts.HEADER_COLOR
import dev.yidafu.blog.fe.views.layouts.TEXT_COLOR_SECONDARY
import io.github.allangomes.kotlinwind.css.LG
import io.github.allangomes.kotlinwind.css.kw
import kotlinx.html.*


fun FlowContent.fullArticle(vo: ArticleVO) {
  article {
    style = kw.inline {
      margin.top[5].bottom[16]
      padding[4]
      border.rounded[LG]
    }

    vo.cover?.let { cover ->
      div("shadow") {
        style = kw.inline {
          flex.row.justify_center.items_center
          padding.x[16].y[3]; margin.y[8]
          background.image[cover].cover.center.no_repeat
          height[80]
        }
      }
    }

    div {
      style = kw.inline {
        width[200].font.size[12].bold;
        text.center.color[HEADER_COLOR.toString()]
      }
      +vo.title
    }
    div {
      style = kw.inline {
        text.center.color[TEXT_COLOR_SECONDARY.toString()]
        margin.top[4]
      }
      span {
        style = kw.inline { font.size[4] }
        +"Hangzhou Xihu District"
      }
      span {
        style = kw.inline { margin.x[4] }
        +"•"
      }
      span {
        style = kw.inline { font.size[4] }
        +"Added By Dov Yih"
      }
      span {
        style = kw.inline { margin.x[4] }
        +"•"
      }
      span {
        style = kw.inline { font.size[4] }
        +"Updated At ${(vo.updatedAt)}"
      }
    }

    div("markdown-body") {
      unsafe {
        +(vo.html ?: "")
      }
    }
  }
}

class ArticleDetailPage(override val vo: ArticleVO) : Page<ArticleVO>() {
  override fun render(): String {
    return FrontendLayout(vo).layout {
      attributes["class"] = "m-auto"
      style = kw.inline { background.white; width[200] }
      fullArticle(vo)
    }.finalize()
  }
}
