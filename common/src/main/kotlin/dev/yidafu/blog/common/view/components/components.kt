package dev.yidafu.blog.common.view.components

import dev.yidafu.blog.common.view.icons.alertErrorIcon
import dev.yidafu.blog.common.view.icons.alertInfoIcon
import dev.yidafu.blog.common.view.icons.alertSuccessIcon
import dev.yidafu.blog.common.view.icons.alertWarningIcon
import io.github.allangomes.kotlinwind.css.*
import kotlinx.html.*

fun FlowContent.footerBlock() {
  footer {
    style =
      kw.inline {
        padding[5]
        background.color["#f7f8f9"]
        flex.row.justify_center.items_center
        text.gray[I300]
      }

    div {
      style = kw.inline { flex.col.items_center }
      img(src = "/public/simple-blog.png") {
        style = kw.inline { width[49] }
      }
      p {
        style = kw.inline { text.stone[I200] }
        +"Copyright © 2023 "
        span {
          style = kw.inline { text.slate[I700] }
          +" Dov Yih "
        }
        +" | Power By "
        span {
          style = kw.inline { text.slate[I700] }
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
  div("alert") {
    style =
      kw.inline {
        when (type) {
          EAlertType.INFO -> text.blue[I200]
          EAlertType.SUCCESS -> background.green[I200]
          EAlertType.WARNING -> background.orange[I200]
          EAlertType.ERROR -> background.red[I200]
        }
        background.red[I200]
        padding.x[6].y[4]
        margin.x[2].y[4]
        border.rounded[4]
        font.size[LG]
        flex.row.items_center
      }
    i {
      style =
        kw.inline {
          width[5].height[5]
          margin.right[3]
          when (type) {
            EAlertType.INFO -> text.blue[I600]
            EAlertType.SUCCESS -> text.green[I600]
            EAlertType.WARNING -> text.orange[I600]
            EAlertType.ERROR -> text.red[I600]
          }
        }
      unsafe {
        val icon =
          when (type) {
            EAlertType.INFO -> alertInfoIcon
            EAlertType.SUCCESS -> alertSuccessIcon
            EAlertType.WARNING -> alertWarningIcon
            EAlertType.ERROR -> alertErrorIcon
          }
        raw(icon.toString())
      }
    }

    span("text-red-800") {
      style =
        kw.inline {
          when (type) {
            EAlertType.INFO -> text.red[I800]
            EAlertType.SUCCESS -> text.red[I800]
            EAlertType.WARNING -> text.red[I800]
            EAlertType.ERROR -> text.red[I800]
          }
        }
      +msg
    }
  }
}
