package dev.yidafu.blog.themes.simple.pages

import de.comahe.i18n4k.Locale
import dev.yidafu.blog.themes.DataModal
import dev.yidafu.blog.themes.Page
import kotlinx.html.*
import kotlinx.html.stream.createHTML as createHTMLX

open class SimplePage(final override val modal: DataModal) : Page {
  protected open val headBlock: HEAD.() -> Unit = {}

  protected open val bodyBlock: BODY.() -> Unit = {}

  protected val locale: Locale = modal.locale

  protected val currentPath: String = modal.path
  protected val siteTitle: String = modal.path
  protected val githubUrl: String = modal.path

  override fun createPageHtml(): String {
    return createHTMLX().apply {
      head {
        link {
          rel = "stylesheet"
          href = "/public/normalize.css"
        }
        link {
          rel = "shortcut icon"
          href = "/public/favicon.ico"
        }
        meta { charset = "UTF-8" }
        title {
          + modal.siteTitle
        }

        style {
          unsafe {
            + defaultHeadStyle.toString()
          }
        }
        link {
          rel = "stylesheet"
          href = "/public/github-markdown-light.css"
        }
        headBlock()
      }

      body(block = bodyBlock)
    }.finalize()
  }
}
