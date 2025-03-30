
package dev.yidafu.blog.themes.blank

import dev.yidafu.blog.themes.DataModal
import dev.yidafu.blog.themes.PageNames
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlinx.serialization.json.*

class BlankTemplateManagerTest : StringSpec({
  fun createDataModal(
    title: String,
    path: String,
  ): DataModal {
    val dataStore =
      mapOf<String, JsonElement>(
        DataModal.SITE_TITLE to JsonPrimitive(title),
        DataModal.CURRENT_PATH to JsonPrimitive(path),
        DataModal.COMMON_LOCALE to JsonPrimitive("en"),
        DataModal.GITHUB_URL to JsonPrimitive("https://github.com/exmaple/repo"),
      )
    return DataModal(Json.encodeToJsonElement(dataStore) as JsonObject)
  }
  "should return correct name" {
    val templateManager = BlankTemplateManager()
    templateManager.getName() shouldBe "Blank Theme"
  }

  "should return correct description" {
    val templateManager = BlankTemplateManager()
    templateManager.getDescription() shouldBe "Blog without any decoration"
  }

  "should create a BlankPage for article list page" {
    val templateManager = BlankTemplateManager()
    val modal = createDataModal("article list", "/articles")
    val page = templateManager.getPageProvider(PageNames.ARTICLE_LIST)!!.createPage(modal)
    page.shouldNotBeNull()
    page.shouldBeInstanceOf<BlankPage>()
    page.createPageHtml() shouldContain "article list"
  }

  "should create a BlankPage for article detail page" {
    val templateManager = BlankTemplateManager()
    val modal = createDataModal("article detail", "/article/1")
    val page = templateManager.getPageProvider(PageNames.ARTICLE_DETAIL)!!.createPage(modal)
    page shouldNotBe null
    page.shouldBeInstanceOf<BlankPage>()
    page.createPageHtml() shouldContain "article detail"
  }

  "should create a BlankPage for about me page" {
    val templateManager = BlankTemplateManager()
    val modal = createDataModal("about me", "about me")
    val page = templateManager.getPageProvider(PageNames.ABOUT_ME)!!.createPage(modal)
    page shouldNotBe null
    page.shouldBeInstanceOf<BlankPage>()
    page.createPageHtml() shouldContain "about me"
  }
})
