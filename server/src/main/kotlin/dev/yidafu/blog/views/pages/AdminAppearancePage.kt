package dev.yidafu.blog.views.pages

import dev.yidafu.blog.common.Routes
import dev.yidafu.blog.bean.vo.AdminAppearanceVo
import dev.yidafu.blog.common.FormKeys
import dev.yidafu.blog.i18n.AdminTxt
import dev.yidafu.blog.views.layouts.AdminLayout
import io.github.allangomes.kotlinwind.css.*
import io.vertx.ext.web.RoutingContext
import kotlinx.html.*

class AdminAppearancePage(private val ctx: RoutingContext) {
  data class FormItem(
    val id: String,
    val label: String,
    val value: String,
    val type: InputType,
    val placeholder: String = "",
  )
  private fun FlowContent.formItem(item: FormItem) {
    div("m-auto") {
      style = kw.inline { margin.bottom[5] }
      label {
        style = kw.inline { block.margin.bottom[2];font.size[LG]; text.gray[I900] }
        htmlFor = item.id
        + item.label
      }
      input {
        style = kw.inline { background.gray[I50]; border[1].rounded[LG].gray[I900]; padding[2]; width[100] }
        type = item.type
        id = item.id
        value = item.value
        name = item.id
        placeholder = item.placeholder
      }
    }
  }
  fun render(vo: AdminAppearanceVo): TagConsumer<String> {
    return AdminLayout(vo).layout {
      h1 {
        + AdminTxt.appearance.toString(vo.locale)
      }
      form {
        action = Routes.APPEARANCE_URL
        method = FormMethod.post
        formItem(FormItem(FormKeys.SITE_TITLE, AdminTxt.site_title.toString(vo.locale), vo.siteTitle, InputType.text))

        formItem(FormItem(FormKeys.GITHUB_URL, AdminTxt.github_url.toString(vo.locale), vo.githubUrl, InputType.url))


        button {
          style = kw.inline { text.white.center; background.blue[I700]; border.rounded[LG]; padding.x[5].y[2] }
          + AdminTxt.submit.toString()
        }
      }
    }
  }
}
