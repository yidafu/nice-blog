package dev.yidafu.blog.themes.simple.pages.front

import dev.yidafu.blog.common.Routes
import dev.yidafu.blog.themes.DataModal
import dev.yidafu.blog.themes.icons.Email
import dev.yidafu.blog.themes.icons.Github
import dev.yidafu.blog.themes.simple.components.footerComponent
import dev.yidafu.blog.themes.simple.pages.SimplePage
import kotlinx.html.*

abstract class FrontPage(modal: DataModal) : SimplePage(modal) {
  abstract fun MAIN.createContent(): Unit

  override val bodyBlock: BODY.() -> Unit = {
    nav {
      classes = setOf("nav")
      div {
        classes = setOf("nav__logo-container")
        a("https://www.yidafu.dev") {
          img {
            classes = setOf("nav__logo")
            src = "/public/logo.svg"
          }
        }
        span {
          classes = setOf("nav__separator")
        }

        div {
          classes = setOf("nav__social")
          a(githubUrl) {
            classes = setOf("nav__social-link")
            Github()
          }

          a("mailto:me@yidafu.dev") {
            classes = setOf("nav__social-link")
            Github()
            Email()
          }
        }
      }

      div {
        classes = setOf("nav__menu")
        a(Routes.ARTICLE_LIST) {
          classes = setOf("nav__menu-link")
          span { +"ARTICLES" }
        }
        a("/about") {
          classes = setOf("nav__menu-link")
          span { +"ABOUT" }
        }
      }
    }

    main {
      classes = setOf("main-content")
      createContent()
    }

    footerComponent()
  }
}
