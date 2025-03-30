package dev.yidafu.blog.themes.simple.pages

import dev.yidafu.blog.common.Routes
import dev.yidafu.blog.common.vo.ArticleVO
import dev.yidafu.blog.i18n.AdminTxt
import dev.yidafu.blog.themes.*
import io.github.allangomes.kotlinwind.css.I300
import io.github.allangomes.kotlinwind.css.I800
import io.github.allangomes.kotlinwind.css.LG
import io.github.allangomes.kotlinwind.css.kw
import kotlinx.html.*


fun FlowContent.readMore(url: String?) {
  a(if (url.isNullOrEmpty()) "/404" else url, classes = "center") {
    button(classes = "read-more center") {
      style =
        kw.inline {
          background.white
          width[70].height[15]
          border[1].rounded[7].gray[I300]
          text.gray[I800]
          font.size[4]
        }

      +"READ MORE"
    }
  }
}

fun FlowContent.partialPost(vo: ArticleVO) {
  AdminTxt.article
  article("post") {
    style =
      kw.inline {
        margin.top[20].bottom[16]
        padding[4]
        border.rounded[LG]
      }
    div {
      style =
        kw.inline {
          width[200].font.size[12].bold
          text.center.color[HEADER_COLOR.toString()]
        }
      +vo.title
    }
    div {
      style =
        kw.inline {
          text.center.color[TEXT_COLOR_SECONDARY.toString()]
          margin.top[4]
        }
      // span {
      //   style = kw.inline { font.size[4] }
      //   +"Hangzhou Xihu District"
      // }
      // span {
      //   style = kw.inline { margin.x[4] }
      //   +"•"
      // }
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

    vo.cover?.let { cover ->
      div("shadow") {
        style =
          kw.inline {
            flex.row.justify_center.items_center
            padding.x[16].y[3]
            margin.y[8]
            background.image[cover].cover.center.no_repeat
            height[80]
          }
      }
    }

    vo.summary?.let { s ->
      div("markdown-body") {
        unsafe {
          +s
        }
      }
    }

    div {
      style =
        kw.inline {
          flex.row.justify_center.items_center
          margin.top[16]
        }
      readMore(vo.identifier?.let { Routes.ARTICLE_DETAIL.replace(":identifier", it) })
    }
  }
}

class ArticleListPage(modal: DataModal) : SimplePage(modal) {
  override val bodyBlock: BODY.() -> Unit = {
    div {
      attributes["class"] = "m-auto"
      style =
        kw.inline {
          background.white
          width[200]
        }
      modal.articleList.forEach { vo ->
        partialPost(vo)
        div {
          style =
            kw.inline {
              height[0.25]
              background.gray[I300]
              margin.y[4]
            }
        }
      }
    }
  }
}

class ArticleListPageProvider: CacheablePageProvider() {
  override fun getName(): String {
    return PageNames.ARTICLE_LIST
  }

  override fun createPage(modal: DataModal): Page {
    return ArticleListPage(modal)
  }
}
