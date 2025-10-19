package dev.yidafu.blog.themes.simple.pages.admin

import dev.yidafu.blog.common.Routes
import dev.yidafu.blog.i18n.AdminTxt
import dev.yidafu.blog.themes.DataModal
import dev.yidafu.blog.themes.icons.*
import dev.yidafu.blog.themes.simple.components.footerComponent
import dev.yidafu.blog.themes.simple.pages.SimplePage
import kotlinx.html.*

fun FlowOrInteractiveOrPhrasingContent.linkItem(
  link: String,
  url: String,
  active: Boolean = false,
) {
  a {
    classes = if (active) {
      setOf("admin-nav__link", "admin-nav__link--active")
    } else {
      setOf("admin-nav__link")
    }
    href = url
    +link
  }
}

abstract class AdminPage(modal: DataModal) : SimplePage(modal) {
  internal fun TR.cell(text: String) {
    td {
      classes = setOf("admin-table__cell")
      +text
    }
  }

  internal fun TR.cell(block: TD.() -> Unit) {
    td {
      classes = setOf("admin-table__cell")
      block()
    }
  }

  private val linkList =
    listOf(
      Triple(AdminTxt.dashboard.toText(), Routes.ADMIN_DASHBOARD_URL, currentPath == Routes.ADMIN_DASHBOARD_URL),
      Triple(
        AdminTxt.article.toText(),
        Routes.ADMIN_ARTICLE_LIST,
        currentPath.startsWith(Routes.ADMIN_ARTICLE_LIST),
      ),
      Triple(
        AdminTxt.configuration.toText(),
        Routes.CONFIGURATION_URL,
        currentPath.startsWith(Routes.CONFIGURATION_URL),
      ),
      Triple(AdminTxt.sync.toText(), Routes.SYNC_URL, currentPath == Routes.SYNC_URL),
      Triple(AdminTxt.pictures.toText(), Routes.PICTURES_URL, currentPath == Routes.PICTURES_URL),
    )

  override val headBlock: HEAD.() -> Unit = {
    script {
      src = "/public/htmx.min.js"
    }
    script {
      src = "/public/htmx-ext-sse.js"
    }
  }

  override val bodyBlock: BODY.() -> Unit = {
    div {
      classes = setOf("admin-header")

      nav {
        classes = setOf("admin-nav")
        div {
          classes = setOf("admin-nav__brand")
          span {
            classes = setOf("admin-nav__title")
            +siteTitle
          }
        }

        div {
          classes = setOf("admin-nav__menu")
          div {
            classes = setOf("admin-nav__links")
            linkList.forEach { item ->
              linkItem(item.first, item.second, item.third)
            }
          }

          div {
            classes = setOf("admin-nav__actions")

            div {
              classes = setOf("dropdown", "admin-nav__lang")
              Language()
              div {
                classes = setOf("dropdown-content", "shadow")
                a {
                  classes = setOf("flex-row", "items-center", "text-gray-600", "px-4", "py-3")
                  href = "?lang=en-US"
                  div {
                    classes = setOf("inline-block", "w-4", "h-4", "mr-2")
                    English()
                  }
                  +AdminTxt.language_english.toText()
                }
                a {
                  classes = setOf("flex-row", "items-center", "text-gray-600", "px-4", "py-3")
                  href = "?lang=zh-CN"
                  div {
                    classes = setOf("inline-block", "w-4", "h-4", "mr-2")
                    Chinese()
                  }
                  +AdminTxt.language_chinese.toText()
                }
              }
            }

            a {
              classes = setOf("admin-nav__github")
              href = githubUrl
              Github()
            }

            a {
              classes = setOf("admin-nav__logout")
              href = Routes.LOGOUT_URL
              span {
                classes = setOf("text-sm")
                +AdminTxt.logout.toText()
              }
              i {
                classes = setOf("admin-nav__logout-icon")
                Logout()
              }
            }
          }
        }
      }
    }

    div {
      classes = setOf("admin-container")
      layoutBlock()
    }

    footerComponent()
  }

  abstract fun DIV.layoutBlock()
}
