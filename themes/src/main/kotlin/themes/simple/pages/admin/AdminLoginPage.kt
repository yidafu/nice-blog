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
import kotlinx.html.*

class AdminLoginPage(modal: DataModal) : SimplePage(modal) {
  private val loginFrom = "login_from"

  override val headBlock: HEAD.() -> Unit = {
    script {
      type = "text/javascript"
      unsafe {
        +JS_LOGIN_CODE
      }
    }
  }

  override val bodyBlock: BODY.() -> Unit = {
    val publicKey = modal.str("publicKey")
    val errorMessage = modal.str("errorMessage")
    div {
      classes = setOf("login-container")
      div {
        classes = setOf("login-form", "shadow-lg")
        // 避免提交表单时，被提交
        formItem(
          FormItem(FormKeys.PUBLIC_KEY, "", publicKey, InputType.hidden),
        )

        form {
          id = loginFrom
          action = Routes.LOGIN_URL
          method = FormMethod.post

          getOptions().forEach { opt ->
            formItem(opt)
          }
          if (errorMessage.isNotEmpty()) {
            alert(errorMessage, EAlertType.ERROR)
          }

          button {
            classes = setOf("btn", "btn--primary", "py-2", "px-4", "rounded")
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
