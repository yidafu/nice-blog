package dev.yidafu.nicemaker.engine.processor

import com.charleskorn.kaml.Yaml
import dev.yidafu.nicemaker.common.dto.CommonArticleDTO
import dev.yidafu.nicemaker.common.dto.FrontMatterDTO
import dev.yidafu.nicemaker.common.modal.ArticleSourceType
import dev.yidafu.nicemaker.engine.ArticleManager
import dev.yidafu.nicemaker.engine.BaseLogger
import dev.yidafu.nicemaker.common.utils.FileTimeUtils
import dev.yidafu.feishu2html.Feishu2Html
import dev.yidafu.feishu2html.Feishu2HtmlOptions
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.io.files.Path
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import java.nio.file.Files
import kotlin.io.path.name
import kotlin.io.path.readText

/**
 * 飞书文档配置
 */
@Serializable
data class FeishuConfig(
  val docxId: String,
  val title: String? = null,
  val cover: String? = null,
  val tags: List<String>? = null,
  val series: String? = null,
  val summary: String? = null,
)

/**
 * 飞书处理器 - JVM平台专用
 *
 * 依赖：
 * - dev.yidafu:feishu2html (JVM-only)
 * - kotlinx.coroutines
 * - java.nio.file API
 *
 * 支持处理 .feishu.yml 配置文件，从飞书拉取文档
 */
class FeishuProcessor(
  private val articleManager: ArticleManager,
  private val logger: BaseLogger,
  private val appId: String,
  private val appSecret: String,
) : IProcessor {

  override fun filter(path: Path): Boolean {
    return path.toString().endsWith(".feishu.yml")
  }

  override fun transform(path: Path): CommonArticleDTO {
    logger.logSync("[Feishu] transform feishu document $path")

    val javaPath = java.nio.file.Paths.get(path.toString())
    val yamlContent = javaPath.readText()

    val config = Yaml.default.decodeFromString<FeishuConfig>(yamlContent)
    val docxId = config.docxId

    logger.logSync("[Feishu] fetching document: $docxId")

    val options = Feishu2HtmlOptions(
      appId = appId,
      appSecret = appSecret
    )
    val feishu2Html = Feishu2Html(options)

    val tempFile = kotlin.io.path.createTempFile(suffix = ".html")

    val htmlContent = try {
      kotlinx.coroutines.runBlocking {
        feishu2Html.export(docxId, tempFile.toString())
      }
      tempFile.toFile().readText()
    } finally {
      Files.deleteIfExists(tempFile)
    }

    val title = config.title ?: extractTitle(htmlContent)
      ?: path.name.removeSuffix(".feishu.yml")

    val cover = config.cover?.let { coverPath ->
      if (coverPath.startsWith("http")) {
        coverPath
      } else {
        val coverPath = Path(path.parent.toString(), coverPath)
        articleManager.processImage(coverPath).toString()
      }
    } ?: ""

    val frontMatterDTO = FrontMatterDTO(
      title = title,
      cover = cover,
      description = config.summary,
      rawContent = yamlContent
    )

    logger.logSync("[Feishu] document transformed: $title")

    val createDate = FileTimeUtils.getCreationTime(path)
    val updateDate = FileTimeUtils.getModifiedTime(path)

    return CommonArticleDTO(
      filename = path.name,
      series = config.series ?: "",
      frontMatter = frontMatterDTO,
      rawContext = yamlContent,
      html = htmlContent,
      createTime = createDate ?: kotlinx.datetime.LocalDateTime(2024, 1, 1, 0, 0),
      updateTime = updateDate ?: kotlinx.datetime.LocalDateTime(2024, 1, 1, 0, 0),
      sourceType = ArticleSourceType.Feishu,
    )
  }

  private fun extractTitle(html: String): String? {
    val titleRegex = Regex("<h1[^>]*>(.*?)</h1>")
    return titleRegex.find(html)?.groupValues?.get(1)?.trim()
  }
}

