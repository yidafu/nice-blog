package dev.yidafu.nicemaker.parser.feishu

import com.charleskorn.kaml.Yaml
import dev.yidafu.nicemaker.core.dto.CommonArticleDTO
import dev.yidafu.nicemaker.core.dto.FrontMatterDTO
import dev.yidafu.nicemaker.core.model.ArticleSourceType
import dev.yidafu.nicemaker.engine.ArticleManager
import dev.yidafu.nicemaker.platform.io.FileTimeUtils
import dev.yidafu.nicemaker.parser.Parser
import dev.yidafu.feishu2html.Feishu2Html
import dev.yidafu.feishu2html.Feishu2HtmlOptions
import dev.yidafu.feishu2html.TemplateMode
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.buffered
import kotlinx.io.readString
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.datetime.Clock

private val logger = KotlinLogging.logger {}

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

class NiceTemplate : TemplateMode
/**
 * 飞书解析器 - KMP版本
 * 使用 Feishu2Html KMP库
 */
class FeishuParser(
  private val articleManager: ArticleManager,
  private val appId: String,
  private val appSecret: String,
) : Parser {

  override fun filter(path: Path): Boolean {
    return path.toString().endsWith(".feishu.yml")
  }

  override fun transform(path: Path): CommonArticleDTO {
    logger.info { "[Feishu] transform feishu document $path" }

    val yamlContent = SystemFileSystem.source(path).buffered().use { it.readString() }

    val config = Yaml.default.decodeFromString<FeishuConfig>(yamlContent)
    val docxId = config.docxId

    logger.info { "[Feishu] fetching document: $docxId" }

    val options = Feishu2HtmlOptions(
      appId = appId,
      appSecret = appSecret,
    )
    val feishu2Html = Feishu2Html(options)

    // 使用跨平台的临时文件方案 - 在文档同目录下创建临时文件
    val tempFileName = ".feishu_temp_${docxId}_${Clock.System.now().toEpochMilliseconds()}.html"
    val tempPath = Path(path.parent.toString(), tempFileName)

    val htmlContent = try {
      kotlinx.coroutines.runBlocking {
        feishu2Html.export(docxId, tempPath.toString())
      }
      SystemFileSystem.source(tempPath).buffered().use { it.readString() }
    } finally {
      // 清理临时文件
      if (SystemFileSystem.exists(tempPath)) {
        SystemFileSystem.delete(tempPath)
      }
    }

    val title = config.title ?: extractTitle(htmlContent)
      ?: path.name.removeSuffix(".feishu.yml")

    val cover = config.cover?.let { coverPath ->
      if (coverPath.startsWith("http")) {
        coverPath
      } else {
        val imgPath = Path(path.parent.toString(), coverPath)
        articleManager.processImage(imgPath).toString()
      }
    } ?: ""

    val frontMatterDTO = FrontMatterDTO(
      title = title,
      cover = cover,
      description = config.summary,
      rawContent = yamlContent
    )

    logger.info { "[Feishu] document transformed: $title" }

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


