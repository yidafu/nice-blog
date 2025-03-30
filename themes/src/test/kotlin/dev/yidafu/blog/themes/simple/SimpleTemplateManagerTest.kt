package dev.yidafu.blog.themes.simple

import dev.yidafu.blog.common.vo.ArticleVO
import dev.yidafu.blog.themes.*
import dev.yidafu.blog.themes.simple.pages.*
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlinx.serialization.json.*
import java.time.LocalDate
import java.time.LocalTime


class SimpleTemplateManagerTest : StringSpec({
  val articleVo = ArticleVO(
    id = 1L,
    title = "Sample Article",
    cover = "https://example.com/cover.jpg",
    identifier = "sample-article",
    series = "Sample Series",
    status = 1,
    summary = "This is a sample article summary.",
    content = "This is the content of the sample article.",
    html = "<p>This is the HTML content of the sample article.</p>",
    updatedAt = LocalDate.now()
  )

  fun createDataModal(title: String, path: String, isList: Boolean = true): DataModal {
    val json = if (isList)
      Json.encodeToJsonElement(listOf(articleVo))
    else
      Json.encodeToJsonElement(articleVo)
    val dataStore = mapOf<String, JsonElement>(
      DataModal.SITE_TITLE to JsonPrimitive(title),
      DataModal.CURRENT_PATH to JsonPrimitive(path),
      DataModal.COMMON_LOCALE to JsonPrimitive("en"),
      DataModal.GITHUB_URL to JsonPrimitive("https://github.com/exmaple/repo"),
      DataModal.VO_DATA to json,
    )
    return DataModal(Json.encodeToJsonElement(dataStore) as JsonObject)
  }

  "should return correct name" {
    val templateManager = SimpleTemplateManager()
    templateManager.getName() shouldBe SimpleTemplateManager.NAME
  }

  "should return correct description" {
    val templateManager = SimpleTemplateManager()
    templateManager.getDescription() shouldBe "Default Template"
  }

  "should register page providers during initialization" {
    val templateManager = SimpleTemplateManager()
    templateManager.getPageProvider(PageNames.ARTICLE_LIST) shouldNotBe null
    templateManager.getPageProvider(PageNames.ARTICLE_DETAIL) shouldNotBe null
    templateManager.getPageProvider(PageNames.ABOUT_ME) shouldNotBe null
  }

  "should return correct page providers" {
    val templateManager = SimpleTemplateManager()
    templateManager.getPageProvider(PageNames.ARTICLE_LIST).shouldBeInstanceOf<ArticleListPageProvider>()
    templateManager.getPageProvider(PageNames.ARTICLE_DETAIL).shouldBeInstanceOf<ArticleDetailPageProvider>()
    templateManager.getPageProvider(PageNames.ABOUT_ME).shouldBeInstanceOf<AboutMePageProvider>()
  }

  "should return not found page when getArticleDetailPage is called" {
    val templateManager = SimpleTemplateManager()
    val modal = createDataModal("article detail", "/article/1", false)
    val page = templateManager.getPageProvider(PageNames.ARTICLE_DETAIL)!!.createPage(modal)
    page.shouldNotBeNull()
    page.createPageHtml() shouldNotBe "This is the HTML content of the sample article."
  }

  "should return not found page when createArticleListPage is called" {
    val templateManager = SimpleTemplateManager()
    val modal = createDataModal("article list", "/article")
    val page = templateManager.getPageProvider(PageNames.ARTICLE_LIST)!!.createPage(modal)
    page.shouldNotBeNull()
    page.createPageHtml() shouldContain "This is a sample article summary."
  }

  "should return not found page when getAboutMePage is called" {
    val templateManager = SimpleTemplateManager()
    val page = templateManager.getNotFoundPage()
    page.shouldNotBeNull()
    page.createPageHtml() shouldContain "<h1>Not Found</h1>"
  }
})
