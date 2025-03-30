package dev.yidafu.blog.themes.simple.pages

import de.comahe.i18n4k.Locale
import de.comahe.i18n4k.messages.MessageBundleLocalizedString
import dev.yidafu.blog.themes.DataModal
import dev.yidafu.blog.themes.Page
import kotlinx.html.*
import kotlinx.html.stream.createHTML

open class SimplePage(final override val modal: DataModal) : Page {
  protected open val headBlock: HEAD.() -> Unit = {}

  protected open val bodyBlock: BODY.() -> Unit = {}

  protected val locale: Locale = modal.locale

  protected val currentPath: String = modal.path
  protected val siteTitle: String = modal.siteTitle
  protected val githubUrl: String = modal.githubUrl

  protected fun MessageBundleLocalizedString.toText(): String {
    return toString(locale)
  }

  override fun createPageHtml(): String {
    return createHTML().apply {
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
          +modal.siteTitle
        }

        style {
          unsafe {
            +defaultHeadStyle.toString()
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
