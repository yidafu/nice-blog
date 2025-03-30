package dev.yidafu.blog.themes.simple.pages

import kotlinx.css.*
import kotlinx.css.properties.BoxShadow
import kotlinx.css.properties.BoxShadows
import kotlinx.css.properties.TextDecoration

val defaultHeadStyle =
  CssBuilder().apply {
    a {
      textDecoration = TextDecoration.none
      visited {
        color = Color.unset
      }
    }
    li {
      listStyleType = ListStyleType.none
    }
    ".m-auto" {
      margin = Margin(LinearDimension.auto)
    }
    ".shadow" {
      val shadow = BoxShadows()
      shadow +=
        BoxShadow(
          Color("rgba(0, 0, 0, 0.1)"),
          0.px,
          1.px,
          3.px,
          0.px,
        )
      shadow +=
        BoxShadow(
          Color("rgba(0, 0, 0, 0.06)"),
          0.px,
          1.px,
          2.px,
          0.px,
        )
      boxShadow = shadow
    }
    ".shadow-lg" {
      val shadow = BoxShadows()
      shadow +=
        BoxShadow(
          Color("rgba(0, 0, 0, 0.1)"),
          0.px,
          10.px,
          15.px,
          (-3).px,
        )
      shadow +=
        BoxShadow(
          Color("rgba(0, 0, 0, 0.05)"),
          0.px,
          4.px,
          6.px,
          (-2).px,
        )
      boxShadow = shadow
    }

    ".dropdown" {
      hover {
        ".dropdown-content" {
          put("display", "block !important")
        }
      }
    }
    ".flex" {
      display = Display.flex
    }
    ".nav-list" {
      not(":last-child") {
        marginBottom = 16.px
      }
    }
    ".markdown-body code.code-cell" {
      padding = Padding(0.px)
    }
  }
