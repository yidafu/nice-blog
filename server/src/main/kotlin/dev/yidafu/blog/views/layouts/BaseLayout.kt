package dev.yidafu.blog.views.layouts

import kotlinx.css.*
import kotlinx.css.properties.BoxShadow
import kotlinx.css.properties.BoxShadows
import kotlinx.css.properties.TextDecoration
import kotlinx.html.*
import kotlinx.html.stream.createHTML

open class BaseLayout : Layout {
  val tagConsumer: TagConsumer<String> = createHTML()
  override fun layout(layoutBlock: DIV.() -> Unit): TagConsumer<String> {
    return tagConsumer.apply {
      head {
        link { rel = "stylesheet";href = "/public/normalize.css" }
        link { rel = "shortcut icon"; href = "/public/favicon.ico" }
        meta { charset = "UTF-8" }
        title {
          + "Dov Yih"
        }
        style {
          unsafe {
            raw(CssBuilder().apply {
              root {
                setCustomProperty("font-size", 16.px)
              }
              kotlinx.css.a {
                textDecoration = TextDecoration.none
                visited {
                  color = Color.unset
                }
              }
              kotlinx.css.li {
                listStyleType = ListStyleType.none
              }
              ".m-auto" {
                margin = Margin(LinearDimension.auto)
              }
              ".shadow" {
                val shadow = BoxShadows();
                shadow += BoxShadow(
                  Color("rgba(0, 0, 0, 0.1)"),
                  0.px,
                  1.px,
                  3.px,
                  0.px,
                )
                shadow  += BoxShadow(
                  Color("rgba(0, 0, 0, 0.06)"),
                  0.px,
                  1.px,
                  2.px,
                  0.px,
                )
                boxShadow =  shadow
              }
              ".shadow-lg" {
                val shadow = BoxShadows();
                shadow += BoxShadow(
                  Color("rgba(0, 0, 0, 0.1)"),
                  0.px,
                  10.px,
                  15.px,
                  (-3).px,
                )
                shadow  += BoxShadow(
                  Color("rgba(0, 0, 0, 0.05)"),
                  0.px,
                  4.px,
                  6.px,
                  (-2).px,
                )
                boxShadow =  shadow
//                box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.1), 0 4px 6px -2px rgba(0, 0, 0, 0.05);
              }
//              kotlinx.css.html { fontSize = 16.px }

              ".dropdown" {
                hover {
                 ".dropdown-content" {
                   put("display", "block !important")
                 }
                }
              }
              ".flex" {
                display = Display.flex;
              }
              ".nav-list" {
                not(":last-child") {
                  marginBottom = 16.px
                }
              }
            }.toString())
          }
        }
      }
      body {
        div {
        layoutBlock()
        }
      }
    }
  }
}
