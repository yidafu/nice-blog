package dev.yidafu.nicemaker.theme.simple.pages.front

import dev.yidafu.nicemaker.core.vo.SeriesVO
import dev.yidafu.nicemaker.theme.CacheablePageProvider
import dev.yidafu.nicemaker.theme.DataModal
import dev.yidafu.nicemaker.theme.Page
import dev.yidafu.nicemaker.theme.PageNames
import kotlinx.html.*

class SeriesListPage(modal: DataModal) : FrontPage(modal) {
  private val seriesList: List<SeriesVO> = modal.list<SeriesVO>("seriesList")

  override fun MAIN.createContent() {
    div {
      classes = setOf("container")

      h1 {
        classes = setOf("page-title")
        +"系列"
      }

      if (seriesList.isEmpty()) {
        p {
          classes = setOf("empty-message")
          +"暂无系列"
        }
      } else {
        div {
          classes = setOf("series-list")

          seriesList.forEach { series ->
            div {
              classes = setOf("series-item")

              h2 {
                classes = setOf("series-title")
                a {
                  href = "/series/${series.id}.html"
                  +series.name
                }
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
            }
          }
        }
      }
    }
  }
}

class SeriesListPageProvider : CacheablePageProvider() {
  override fun getName(): String = PageNames.SERIES_LIST

  override fun createPage(modal: DataModal): Page {
    return SeriesListPage(modal)
  }
}

