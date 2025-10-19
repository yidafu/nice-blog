package dev.yidafu.nicemaker.themes.simple.pages.front

import dev.yidafu.nicemaker.common.vo.ArticleVO
import dev.yidafu.nicemaker.common.vo.SeriesVO
import dev.yidafu.nicemaker.themes.CacheablePageProvider
import dev.yidafu.nicemaker.themes.DataModal
import dev.yidafu.nicemaker.themes.Page
import dev.yidafu.nicemaker.themes.PageNames
import kotlinx.html.*

class SeriesDetailPage(modal: DataModal) : FrontPage(modal) {
  private val series: SeriesVO = modal.obj<SeriesVO>("series") ?: SeriesVO(
    id = "",
    name = "未知系列",
    description = "",
    articleCount = 0,
    articles = emptyList()
  )

  override fun MAIN.createContent() {
    div {
      classes = setOf("container")

      h1 {
        classes = setOf("page-title")
        +series.name
      }

      if (series.description.isNotEmpty()) {
        p {
          classes = setOf("series-description")
          +series.description
        }
      }

      p {
        classes = setOf("series-meta")
        +"共 ${series.articleCount} 篇文章"
      }

      div {
        classes = setOf("series-articles")

        series.articles.forEachIndexed { index, article ->
          div {
            classes = setOf("series-article-item")

            span {
              classes = setOf("article-order")
              +"${index + 1}."
            }

            a {
              href = "/articles/${article.identifier ?: article.id}.html"
              classes = setOf("article-title")
              +article.title
            }

            if (article.summary?.isNotEmpty() == true) {
              p {
                classes = setOf("article-description")
                +article.summary!!
              }
            }

            p {
              classes = setOf("article-meta")
              +"${article.createdAt}"
            }
          }
        }
      }
    }
  }
}

class SeriesDetailPageProvider : CacheablePageProvider() {
  override fun getName(): String = PageNames.SERIES_DETAIL

  override fun createPage(modal: DataModal): Page {
    return SeriesDetailPage(modal)
  }
}

