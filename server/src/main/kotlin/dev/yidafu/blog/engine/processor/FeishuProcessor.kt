package dev.yidafu.blog.engine.processor

import com.charleskorn.kaml.Yaml
import dev.yidafu.blog.common.dto.CommonArticleDTO
import dev.yidafu.blog.common.dto.FrontMatterDTO
import dev.yidafu.blog.common.modal.ArticleSourceType
import dev.yidafu.blog.engine.*
import dev.yidafu.blog.engine.ext.getGitCreateTime
import dev.yidafu.blog.engine.ext.getGitModifyTime
import dev.yidafu.feishu2html.Feishu2Html
import dev.yidafu.feishu2html.Feishu2HtmlOptions
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.name
import kotlin.io.path.readText

@Serializable
data class FeishuConfig(
  val docxId: String,
  val title: String? = null,
  val cover: String? = null,
  val tags: List<String>? = null,
  val series: String? = null,
  val summary: String? = null,
)

class FeishuProcessor(
  private val articleManager: ArticleManager,
  private val logger: BaseLogger,
  private val appId: String,
  private val appSecret: String,
) : IProcessor {

  override fun filter(path: Path): Boolean {
    // 匹配 .feishu.yml 后缀
    return path.toString().endsWith(".feishu.yml")
  }

  override fun transform(path: Path): CommonArticleDTO {
    logger.logSync("[Feishu] transform feishu document $path")

    val file = path.toFile()
    val yamlContent = path.readText()

    // 解析 YAML 文件
    val config = Yaml.default.decodeFromString<FeishuConfig>(yamlContent)
    val docxId = config.docxId

    logger.logSync("[Feishu] fetching document: $docxId")
    
    // TODO: 根据 feishu2html 实际 API 调整
    // 目前 feishu2html 库可能需要导出到文件，然后再读取
    // 这里先创建一个临时文件处理
    val options = Feishu2HtmlOptions(
      appId = appId,
      appSecret = appSecret
    )
    val feishu2Html = Feishu2Html(options)
    
    // 创建临时文件
    val tempFile = kotlin.io.path.createTempFile(suffix = ".html")
    
    val htmlContent = try {
      kotlinx.coroutines.runBlocking {
        feishu2Html.export(docxId, tempFile.toString())
      }
      tempFile.toFile().readText()
    } finally {
      Files.deleteIfExists(tempFile)
    }

    // 优先使用 YAML 中的标题，否则从 HTML 提取
    val title = config.title ?: extractTitle(htmlContent) 
      ?: path.name.removeSuffix(".feishu.yml")
    
    // 处理封面图片
    val cover = config.cover?.let { coverPath ->
      if (coverPath.startsWith("http")) {
        coverPath
      } else {
        val coverFile = path.parent.resolve(coverPath).toFile()
        articleManager.processImage(coverFile).toString()
      }
    } ?: ""
    
    val frontMatterDTO = FrontMatterDTO(
      title = title,
      cover = cover,
      description = config.summary,
      rawContent = yamlContent
    )

    logger.logSync("[Feishu] document transformed: $title")
    
    return CommonArticleDTO(
      filename = path.name,
      series = config.series ?: "",
      frontMatter = frontMatterDTO,
      rawContext = yamlContent, // 存储原始 YAML 内容
      html = htmlContent,
      createTime = path.getGitCreateTime(),
      updateTime = path.getGitModifyTime(),
      sourceType = ArticleSourceType.Feishu,
    )
  }

  private fun extractTitle(html: String): String? {
    // 简单的标题提取（从第一个h1标签）
    val h1Regex = Regex("<h1[^>]*>(.*?)</h1>")
    return h1Regex.find(html)?.groupValues?.get(1)?.replace(Regex("<[^>]+>"), "")
  }
}

