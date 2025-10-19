package dev.yidafu.nicemaker.themes.simple.pages.front

import de.comahe.i18n4k.strings.asLocalizedString
import de.comahe.i18n4k.strings.toString
import dev.yidafu.nicemaker.common.Routes
import dev.yidafu.nicemaker.common.ext.formatString
import dev.yidafu.nicemaker.common.vo.ArticleVO
import dev.yidafu.nicemaker.i18n.AdminTxt
import dev.yidafu.nicemaker.themes.*
import kotlinx.html.*

fun FlowContent.readMore(url: String?, locale: de.comahe.i18n4k.Locale) {
  a(if (url.isNullOrEmpty()) "/404" else url) {
    classes = setOf("article-card__footer")
    button {
      classes = setOf("btn", "btn--read-more")

      +AdminTxt.read_more.toString(locale)
    }
  }
}

fun FlowContent.partialPost(vo: ArticleVO, locale: de.comahe.i18n4k.Locale) {
  article {
    classes = setOf("article-card")
    div {
      classes = setOf("article-card__title")
      +vo.title
    }
    div {
      classes = setOf("article-card__meta")
      span {
        classes = setOf("article-card__meta-item")
        +"${AdminTxt.author.toString(locale)}: Dov Yih"
      }
      span {
        classes = setOf("article-card__meta-separator")
        +"•"
      }
      span {
        classes = setOf("article-card__meta-item")
        +"${AdminTxt.updated_at.toString(locale)} ${vo.updatedAt.formatString()}"
      }
    }

    vo.cover?.let { cover ->
      div {
        classes = setOf("article-card__cover", "shadow")
        attributes["style"] = "background-image: url('$cover');"
      }
    }

    vo.summary?.let { s ->
      div {
        classes = setOf("markdown-body", "article-card__summary")
        unsafe {
          +s
        }
      }
    }

    div {
      classes = setOf("article-card__footer")
      readMore(vo.identifier?.let { Routes.ARTICLE_DETAIL.replace(":identifier", it) }, locale)
    }
  }
}

class ArticleListPage(modal: DataModal) : FrontPage(modal) {
  override fun MAIN.createContent() {
    div {
      classes = setOf("m-auto", "bg-white", "w-200")
      val articles = modal.list<ArticleVO>("articles")
      articles.forEach { vo ->
        partialPost(vo, locale)
        div {
          classes = setOf("divider")
        }
      }
    }
  }
}

class ArticleListPageProvider : CacheablePageProvider() {
  override fun getName(): String {
    return PageNames.ARTICLE_LIST
  }

  override fun createPage(modal: DataModal): Page {
    return ArticleListPage(modal)
  }
}
