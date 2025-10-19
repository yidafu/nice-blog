package dev.yidafu.nicemaker.themes.simple.pages.error

import de.comahe.i18n4k.messages.MessageBundleLocalizedString
import dev.yidafu.nicemaker.i18n.AdminTxt
import dev.yidafu.nicemaker.themes.DataModal
import dev.yidafu.nicemaker.themes.simple.pages.SimplePage
import kotlinx.html.*

abstract class ErrorPage(
  modal: DataModal,
  private val errorCode: Int,
  private val errorTitle: MessageBundleLocalizedString,
  private val errorDescription: MessageBundleLocalizedString,
) : SimplePage(modal) {

  override val headBlock: HEAD.() -> Unit = {
    title { +"$errorCode - ${errorTitle.toText()}" }
  }

  override val bodyBlock: BODY.() -> Unit = {
    div {
      classes = setOf("flex", "flex-col", "items-center", "justify-center", "min-h-screen", "bg-gray-50")
      style = "padding: 2rem;"

      div {
        classes = setOf("text-center")
        style = "max-width: 600px;"

        // 错误码
        h1 {
          classes = setOf("text-9xl", "font-bold", "text-blue-500", "mb-4")
          +errorCode.toString()
        }

        // 错误标题
        h2 {
          classes = setOf("text-3xl", "font-bold", "text-gray-800", "mb-4")
          +errorTitle.toText()
        }

        // 错误描述
        p {
          classes = setOf("text-lg", "text-gray-600", "mb-8")
          +errorDescription.toText()
        }

        // 返回按钮
        div {
          classes = setOf("flex", "gap-4", "justify-center")

          a {
            href = "/"
            classes = setOf("btn", "btn-primary")
            +AdminTxt.go_home.toText()
          }

          a {
            href = "javascript:history.back()"
            classes = setOf("btn")
            +AdminTxt.go_back.toText()
          }
        }
      }
    }
  }
}

