package dev.yidafu.nicemaker.generator

import de.comahe.i18n4k.Locale
import dev.yidafu.nicemaker.common.TemplateManagerLoader
import dev.yidafu.nicemaker.common.dto.CommonArticleDTO
import dev.yidafu.nicemaker.common.vo.ArticleVO
import dev.yidafu.nicemaker.themes.*
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.datetime.LocalDateTime
import kotlinx.html.*
import kotlinx.html.stream.appendHTML
import kotlinx.io.Buffer
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.readString
import kotlinx.io.readTo
import kotlinx.io.writeString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.encodeToJsonElement

private val logger = KotlinLogging.logger {}

/**
 * 页面生成器
 * 负责生成所有静态 HTML 页面
 */
class PageGenerator(
  private val articles: List<CommonArticleDTO>,
  private val config: SiteConfig,
) {
  private val templateManager = TemplateManagerLoader.getTemplateManager(
    "Simple Theme",
  )

  fun generate() {
    val outputDir = Path(config.build.output)

    // 清理输出目录
    if (config.build.cleanBeforeBuild && SystemFileSystem.exists(outputDir)) {
      logger.info { "Cleaning output directory..." }
      deleteRecursively(outputDir)
    }
    SystemFileSystem.createDirectories(outputDir)

    logger.info { "Generating static site to: $outputDir" }

    // 转换为 VO
    val articleVOs =
      articles.map { dto -> dtoToVO(dto) }
        .sortedByDescending { vo -> vo.updatedAt }

    // 1. 生成首页
    generateIndexPage(outputDir, articleVOs)

    // 2. 生成文章列表分页
    generateArticleListPages(outputDir, articleVOs)

    // 3. 生成文章详情页
    generateArticleDetailPages(outputDir, articleVOs)

    // 4. 生成标签页面
    generateTagListPage(outputDir, articles)

    // 5. 生成系列页面
    generateSeriesPages(outputDir, articles)

    // 6. 生成关于我页面
    generateAboutMePage(outputDir)

    // 7. 生成错误页面
    generateErrorPages(outputDir)

    // 8. 复制静态资源
    copyStaticAssets(outputDir)

    // 9. 生成 sitemap
    generateSitemap(outputDir, articleVOs)

    logger.info { "✅ Site generated successfully!" }
    logger.info { "   Total pages: ${articleVOs.size + 1}" }
    logger.info { "   Output: $outputDir" }
  }

  /**
   * 递归删除目录
   */
  private fun deleteRecursively(path: Path) {
    if (!SystemFileSystem.exists(path)) return

    val metadata = SystemFileSystem.metadataOrNull(path) ?: return

    if (metadata.isDirectory) {
      SystemFileSystem.list(path).forEach { child ->
        deleteRecursively(child)
      }
    }

    SystemFileSystem.delete(path)
  }

  private fun generateIndexPage(
    outputDir: Path,
    articles: List<ArticleVO>,
  ) {
    logger.info { "Generating index page..." }

    val html =
      renderPage(
        pageName = PageNames.ARTICLE_LIST,
        data = mapOf("articles" to articles.take(config.pagination.pageSize)),
      )

    writeFile(Path(outputDir.toString() + "/index.html"), html)
  }

  private fun generateArticleListPages(
    outputDir: Path,
    articles: List<ArticleVO>,
  ) {
    logger.info { "Generating article list pages..." }

    val articlesDir = Path(outputDir.toString() + "/articles")
    SystemFileSystem.createDirectories(articlesDir)

    val pageSize = config.pagination.pageSize
    val totalPages = (articles.size + pageSize - 1) / pageSize

    for (page in 1..totalPages) {
      val start = (page - 1) * pageSize
      val end = minOf(start + pageSize, articles.size)
      val pageArticles = articles.subList(start, end)

      val html =
        renderPage(
          pageName = PageNames.ARTICLE_LIST,
          data =
            mapOf(
              "articles" to pageArticles,
              "currentPage" to page,
              "totalPages" to totalPages,
            ),
        )

      val filename = if (page == 1) "index.html" else "page-$page.html"
      writeFile(Path(articlesDir.toString() + "/$filename"), html)
    }

    logger.info { "✓ Generated $totalPages list pages" }
  }

  private fun generateArticleDetailPages(
    outputDir: Path,
    articles: List<ArticleVO>,
  ) {
    logger.info { "Generating article detail pages..." }

    val articlesDir = Path(outputDir.toString() + "/articles")
    SystemFileSystem.createDirectories(articlesDir)

    articles.forEach { article ->
      val html =
        renderPage(
          pageName = PageNames.ARTICLE_DETAIL,
          data = mapOf("article" to article),
        )

      val filename = "${article.identifier}.html"
      writeFile(Path(articlesDir.toString() + "/$filename"), html)
    }

    logger.info { "✓ Generated ${articles.size} detail pages" }
  }

  private fun generateErrorPages(outputDir: Path) {
    logger.info { "Generating error pages..." }

    listOf(
      PageNames.ERROR_404 to "404.html",
      PageNames.ERROR_500 to "500.html",
    ).forEach { (pageName, filename) ->
      val html = renderPage(pageName, emptyMap())
      writeFile(Path(outputDir.toString() + "/$filename"), html)
    }
  }

  private fun copyStaticAssets(outputDir: Path) {
    logger.info { "Copying static assets..." }

    val publicDir = Path("themes/src/main/resources/public")
    if (SystemFileSystem.exists(publicDir)) {
      copyRecursively(publicDir, Path(outputDir.toString() + "/public"))
      logger.debug { "Copied static assets from: $publicDir" }
    }
  }

  /**
   * 递归复制目录
   */
  private fun copyRecursively(source: Path, target: Path) {
    if (!SystemFileSystem.exists(source)) return

    val metadata = SystemFileSystem.metadataOrNull(source) ?: return

    if (metadata.isRegularFile) {
      target.parent?.let { parent ->
        if (!SystemFileSystem.exists(parent)) {
          SystemFileSystem.createDirectories(parent)
        }
      }
      // 使用source和sink进行文件复制
      SystemFileSystem.source(source).use { sourceStream ->
        SystemFileSystem.sink(target).use { targetStream ->
          val buffer = Buffer()
          while (true) {
            buffer.clear()
            val bytesRead = sourceStream.readAtMostTo(buffer, 8192)
            if (bytesRead == -1L) break
            targetStream.write(buffer, bytesRead)
          }
        }
      }
    } else if (metadata.isDirectory) {
      SystemFileSystem.createDirectories(target)
      SystemFileSystem.list(source).forEach { child ->
        val childName = child.name
        copyRecursively(child, Path(target.toString() + "/$childName"))
      }
    }
  }

  private fun generateTagListPage(
    outputDir: Path,
    articles: List<CommonArticleDTO>,
  ) {
    logger.info { "Generating tag list page..." }

    // 聚合标签
    val tags = articles
      .flatMap { dto -> dto.frontMatter?.tags ?: emptyList() }
      .groupBy { it }
      .map { (tag, list) ->
        mapOf(
          "name" to tag,
          "count" to list.size,
          "slug" to tag.lowercase().replace(" ", "-")
        )
      }
      .sortedByDescending { it["count"] as Int }

    val html = renderPage(
      pageName = PageNames.TAG_LIST,
      data = mapOf("tags" to tags),
    )

    writeFile(Path(outputDir.toString() + "/tags.html"), html)
  }

  private fun generateSeriesPages(
    outputDir: Path,
    articles: List<CommonArticleDTO>,
  ) {
    logger.info { "Generating series pages..." }

    // 聚合系列
    val seriesMap = articles
      .filter { it.series.isNotBlank() }
      .groupBy { it.series }
      .mapValues { (_, dtos) ->
        dtos.sortedBy { it.frontMatter?.seriesOrder ?: 0 }
          .map { dtoToVO(it) }
      }

    val seriesList = seriesMap.map { (name, articleVOs) ->
      mapOf(
        "id" to name.lowercase().replace(" ", "-"),
        "name" to name,
        "description" to "",
        "articleCount" to articleVOs.size,
        "articles" to articleVOs
      )
    }

    // 生成系列列表页
    val listHtml = renderPage(
      pageName = PageNames.SERIES_LIST,
      data = mapOf("seriesList" to seriesList),
    )
    writeFile(Path(outputDir.toString() + "/series.html"), listHtml)

    // 生成每个系列的详情页
    val seriesDir = Path(outputDir.toString() + "/series")
    SystemFileSystem.createDirectories(seriesDir)

    seriesMap.forEach { (name, articleVOs) ->
      val seriesId = name.lowercase().replace(" ", "-")
      val detailHtml = renderPage(
        pageName = PageNames.SERIES_DETAIL,
        data = mapOf(
          "series" to mapOf(
            "id" to seriesId,
            "name" to name,
            "description" to "",
            "articleCount" to articleVOs.size,
            "articles" to articleVOs
          )
        ),
      )
      writeFile(Path(seriesDir.toString() + "/$seriesId.html"), detailHtml)
    }

    logger.info { "✓ Generated ${seriesMap.size} series pages" }
  }

  private fun generateAboutMePage(outputDir: Path) {
    logger.info { "Generating about me page..." }

    // AboutMe 内容从 StaticSiteGenerator 传入
    val aboutContent = config.aboutContent ?: ""
    logger.debug { "AboutMe content length: ${aboutContent.length}" }

    val html = renderPage(
      pageName = PageNames.ABOUT_ME,
      data = mapOf("aboutContent" to aboutContent),
    )

    writeFile(Path(outputDir.toString() + "/about.html"), html)
  }

  private fun generateSitemap(
    outputDir: Path,
    articles: List<ArticleVO>,
  ) {
    logger.info { "Generating sitemap.xml..." }

    val sitemap =
      buildString {
        appendLine("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
        appendLine("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">")

        // 首页
        appendLine("  <url>")
        appendLine("    <loc>${config.site.url}/</loc>")
        appendLine("    <changefreq>daily</changefreq>")
        appendLine("    <priority>1.0</priority>")
        appendLine("  </url>")

        // 标签页
        appendLine("  <url>")
        appendLine("    <loc>${config.site.url}/tags.html</loc>")
        appendLine("    <changefreq>weekly</changefreq>")
        appendLine("    <priority>0.7</priority>")
        appendLine("  </url>")

        // 系列页
        appendLine("  <url>")
        appendLine("    <loc>${config.site.url}/series.html</loc>")
        appendLine("    <changefreq>weekly</changefreq>")
        appendLine("    <priority>0.7</priority>")
        appendLine("  </url>")

        // 关于页
        appendLine("  <url>")
        appendLine("    <loc>${config.site.url}/about.html</loc>")
        appendLine("    <changefreq>monthly</changefreq>")
        appendLine("    <priority>0.6</priority>")
        appendLine("  </url>")

        // 文章页
        articles.forEach { article ->
          appendLine("  <url>")
          appendLine("    <loc>${config.site.url}/articles/${article.identifier}.html</loc>")
          appendLine("    <lastmod>${article.updatedAt}</lastmod>")
          appendLine("    <changefreq>weekly</changefreq>")
          appendLine("    <priority>0.8</priority>")
          appendLine("  </url>")
        }

        appendLine("</urlset>")
      }

    writeFile(Path(outputDir.toString() + "/sitemap.xml"), sitemap)
  }

  private fun renderPage(
    pageName: String,
    data: Map<String, Any>,
  ): String {
    val locale = de.comahe.i18n4k.Locale(config.site.language)

    // 构建 DataModal
    val dataMap = mutableMapOf<String, kotlinx.serialization.json.JsonElement>()
    dataMap[DataModal.COMMON_LOCALE] = Json.encodeToJsonElement(config.site.language)  // 直接使用语言字符串
    dataMap[DataModal.CURRENT_PATH] = Json.encodeToJsonElement("/")
    dataMap[DataModal.SITE_TITLE] = Json.encodeToJsonElement(config.site.title)
    dataMap[DataModal.GITHUB_URL] = Json.encodeToJsonElement("")

    // 处理自定义数据，放到 VO_DATA 下
    val voDataMap = mutableMapOf<String, kotlinx.serialization.json.JsonElement>()

    data.forEach { (key, value) ->
      val jsonElement = when (value) {
        is List<*> -> {
          // 处理列表：可能是 ArticleVO, TagVO, SeriesVO 或 Map
          if (value.isNotEmpty()) {
            when (value.first()) {
              is ArticleVO -> Json.encodeToJsonElement(value as List<ArticleVO>)
              is Map<*, *> -> {
                // 手动构建 JsonArray，处理 Map<String, Any>
                val jsonArray = value.map { item ->
                  val mapItem = item as Map<*, *>
                  val jsonMap = mutableMapOf<String, kotlinx.serialization.json.JsonElement>()
                  mapItem.forEach { (k, v) ->
                    jsonMap[k.toString()] = when (v) {
                      is String -> Json.encodeToJsonElement(v)
                      is Int -> Json.encodeToJsonElement(v)
                      is List<*> -> Json.encodeToJsonElement(v as List<ArticleVO>)
                      else -> Json.encodeToJsonElement(v.toString())
                    }
                  }
                  JsonObject(jsonMap)
                }
                kotlinx.serialization.json.JsonArray(jsonArray)
              }
              else -> Json.encodeToJsonElement(value.toString())
            }
          } else {
            Json.encodeToJsonElement(emptyList<String>())
          }
        }
        is ArticleVO -> Json.encodeToJsonElement(value)
        is Map<*, *> -> {
          // 手动构建 JsonObject
          val mapValue = value as Map<*, *>
          val jsonMap = mutableMapOf<String, kotlinx.serialization.json.JsonElement>()
          mapValue.forEach { (k, v) ->
            jsonMap[k.toString()] = when (v) {
              is String -> Json.encodeToJsonElement(v)
              is Int -> Json.encodeToJsonElement(v)
              is List<*> -> Json.encodeToJsonElement(v as List<ArticleVO>)
              else -> Json.encodeToJsonElement(v.toString())
            }
          }
          JsonObject(jsonMap)
        }
        is String -> Json.encodeToJsonElement(value)
        is Int -> Json.encodeToJsonElement(value)
        else -> Json.encodeToJsonElement(value.toString())
      }
      voDataMap[key] = jsonElement
      logger.debug { "renderPage: $key = ${if (value is String) value.take(50) else value::class.simpleName}" }
    }

    dataMap[DataModal.VO_DATA] = JsonObject(voDataMap)
    val dataStore = JsonObject(dataMap)

    val modal = DataModal(dataStore)

    // 获取页面提供者
    val pageProvider =
      templateManager.getPageProvider(pageName)
        ?: throw IllegalArgumentException("Page provider not found: $pageName")

    // 创建页面
    val page = pageProvider.createPage(modal)

    // 渲染为 HTML 字符串
    return buildString {
      appendHTML().html {
        page.render(this)
      }
    }
  }

  private fun writeFile(
    file: Path,
    content: String,
  ) {
    file.parent?.let { parent ->
      if (!SystemFileSystem.exists(parent)) {
        SystemFileSystem.createDirectories(parent)
      }
    }
    SystemFileSystem.sink(file).buffered().use { sink ->
      sink.writeString(content)
    }
  }

  /**
   * 将 CommonArticleDTO 转换为 ArticleVO
   */
  private fun dtoToVO(dto: CommonArticleDTO): ArticleVO {
    // 从文件名生成 identifier
    val identifier = dto.filename
      .substringBeforeLast('.')
      .replace(Regex("^\\d{4}-\\d{2}-\\d{2}-"), "")  // 移除日期前缀

    return ArticleVO(
      id = 0,  // 静态生成不需要 ID
      title = dto.frontMatter?.title ?: dto.filename,
      cover = dto.frontMatter?.cover,
      identifier = identifier,
      series = dto.series.takeIf { it.isNotBlank() },
      status = 1,  // 已发布
      summary = dto.frontMatter?.description,
      content = dto.rawContext,
      html = dto.html,
      createdAt = dto.createTime,
      updatedAt = dto.updateTime,
    )
  }
}

