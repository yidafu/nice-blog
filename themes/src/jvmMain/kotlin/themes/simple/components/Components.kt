package dev.yidafu.nicemaker.theme.simple.components

import dev.yidafu.nicemaker.theme.icons.*
import kotlinx.html.*

fun FlowContent.footerComponent() {
  footer {
    classes = setOf("footer")
    div {
      classes = setOf("footer__content")
      img(src = "/public/simple-blog.png") {
        classes = setOf("footer__logo")
      }
      p {
        classes = setOf("footer__text")
        +"Copyright © 2023 "
        span {
          classes = setOf("footer__text--highlight")
          +" Dov Yih "
        }
        +" | Power By "
        span {
          classes = setOf("footer__text--highlight")
          +" Kotlin"
        }
      }
    }
  }
}

enum class EAlertType {
  SUCCESS,
  INFO,
  WARNING,
  ERROR,
}

/**
 * https://tailwindflex.com/@prashant/basic-alert-success-info-warning-error
 */
fun FlowContent.alert(
  msg: String,
  type: EAlertType = EAlertType.INFO,
) {
  val alertClass =
    when (type) {
      EAlertType.INFO -> "alert--info"
      EAlertType.SUCCESS -> "alert--success"
      EAlertType.WARNING -> "alert--warning"
      EAlertType.ERROR -> "alert--error"
    }

  div {
    classes = setOf("alert", alertClass)
    i {
      classes = setOf("alert__icon")
      consumer.onTagContentUnsafe {
        raw(
          when (type) {
            EAlertType.INFO -> alertInfoIcon.toString()
            EAlertType.SUCCESS -> alertSuccessIcon.toString()
            EAlertType.WARNING -> alertWarningIcon.toString()
            EAlertType.ERROR -> alertErrorIcon.toString()
          },
        )
      }
    }
    div {
      +msg
    }
  }
}

fun FlowContent.Button(block: DIV.() -> Unit) {
  div {
    classes = setOf("btn")
    block()
  }
}
