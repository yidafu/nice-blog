package dev.yidafu.blog.themes.simple.pages.front

import dev.yidafu.blog.common.vo.ArticleVO
import dev.yidafu.blog.themes.*
import kotlinx.html.*

fun FlowContent.fullArticle(vo: ArticleVO) {
  article {
    classes = setOf("article-card")

    vo.cover?.let { cover ->
      div {
        classes = setOf("article-card__cover", "shadow")
        attributes["style"] = "background-image: url('$cover');"
      }
    }

    div {
      classes = setOf("article-card__title")
      +vo.title
    }
    div {
      classes = setOf("article-card__meta")
      span {
        classes = setOf("article-card__meta-item")
        +"Added By Dov Yih"
      }
      span {
        classes = setOf("article-card__meta-separator")
        +"•"
      }
      span {
        classes = setOf("article-card__meta-item")
        +"Updated At ${(vo.updatedAt)}"
      }
    }

    div {
      classes = setOf("markdown-body")
      unsafe {
        +(vo.html ?: "")
      }
    }
  }
}

class ArticleDetailPage(modal: DataModal) : FrontPage(modal) {
  override fun MAIN.createContent() {
    div {
      classes = setOf("m-auto", "bg-white", "w-200")
      val article = modal.obj<ArticleVO>("article")
      if (article != null) {
        fullArticle(article)
      }
    }
  }
}

class ArticleDetailPageProvider : CacheablePageProvider() {
  override fun getName(): String {
    return PageNames.ARTICLE_DETAIL
  }

  override fun createPage(modal: DataModal): Page {
    return ArticleDetailPage(modal)
  }
}
