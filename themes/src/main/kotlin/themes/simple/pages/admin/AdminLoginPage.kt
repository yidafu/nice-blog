package dev.yidafu.blog.themes.simple.pages.admin

import dev.yidafu.blog.common.FormKeys
import dev.yidafu.blog.common.Routes
import dev.yidafu.blog.i18n.AdminTxt
import dev.yidafu.blog.themes.*
import dev.yidafu.blog.themes.loginVo
import dev.yidafu.blog.themes.simple.components.EAlertType
import dev.yidafu.blog.themes.simple.components.FormItem
import dev.yidafu.blog.themes.simple.components.alert
import dev.yidafu.blog.themes.simple.components.formItem
import dev.yidafu.blog.themes.simple.pages.SimplePage
import io.github.allangomes.kotlinwind.css.I50
import io.github.allangomes.kotlinwind.css.I500
import io.github.allangomes.kotlinwind.css.I700
import io.github.allangomes.kotlinwind.css.kw
import kotlinx.html.*
import java.io.FileNotFoundException

fun readResource(filename: String): String {
  return SimplePage::class.java.classLoader
    .getResourceAsStream(filename)
    ?.bufferedReader()?.readText() ?: throw FileNotFoundException(filename)
}

private val loginJsCode by lazy {
  readResource("META-INF/loginCode.js")
}

class AdminLoginPage(modal: DataModal) : SimplePage(modal) {
  private val loginFrom = "login_from"

  override val headBlock: HEAD.() -> Unit = {
    script {
      type = "text/javascript"
      unsafe {
        +loginJsCode
      }
    }
  }

  override val bodyBlock: BODY.() -> Unit = {
    val vo = modal.loginVo
    div {
      style =
        kw.inline {
          flex.col.grow.justify_center.items_center
          width["100%"].height["100%"]
          background.slate[I50]
        }
      div("shadow-lg") {
        style =
          kw.inline {
            width[100].padding[4]
            border.rounded[8]
            background.white
          }
        // 避免提交表单时，被提交
        formItem(
          FormItem(FormKeys.PUBLIC_KEY, "", vo.publicKey, InputType.hidden),
        )

        form {
          id = loginFrom
          action = Routes.LOGIN_URL
          method = FormMethod.post
          formItem(
            FormItem(
              FormKeys.REDIRECT_URL,
              "",
              vo.redirectUrl ?: "",
              InputType.hidden,
            ),
          )
          getOptions().forEach { opt ->
            formItem(opt)
          }
          if (vo.errorMessage != null) {
            alert(vo.errorMessage!!, EAlertType.ERROR)
          }

          button {
            style =
              kw.inline {
                background.blue[I500]
                text.white
                font.bold
                padding.y[2].x[4]
                border.blue[I700].rounded[4]
              }
            id = FormKeys.SUBMIT
            +AdminTxt.submit.toText()
          }
        }
      }
    }
  }

  private fun getOptions() =
    listOf(
      FormItem(
        FormKeys.USER_NAME,
        AdminTxt.username.toText(),
        "",
        InputType.text,
        AdminTxt.username_placeholder.toText(),
        true,
      ),
      FormItem(
        FormKeys.PASSWORD,
        AdminTxt.password.toText(),
        "",
        InputType.password,
        AdminTxt.password_placeholder.toText(),
        true,
      ),
    )
}

class AdminLoginPageProvider : CacheablePageProvider() {
  override fun getName(): String = PageNames.ADMIN_LOGIN

  override fun createPage(modal: DataModal): Page = AdminLoginPage(modal)
}
