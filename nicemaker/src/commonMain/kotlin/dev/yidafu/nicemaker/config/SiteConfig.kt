package dev.yidafu.nicemaker.config

import com.charleskorn.kaml.Yaml
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.readString
import kotlinx.serialization.Serializable
private val logger = KotlinLogging.logger {"SiteConfig"}

/**
 * 获取环境变量
 * @param name 环境变量名
 * @return 环境变量值，如果不存在则返回 null
 */
expect fun getEnvVar(name: String): String?

/**
 * 替换字符串中的环境变量
 * 格式：${VAR_NAME}
 */
fun replaceEnvVars(content: String): String {
  val regex = Regex("\\$\\{([^}]+)}")
  return regex.replace(content) { match ->
    val envVarName = match.groupValues[1]
    getEnvVar(envVarName) ?: match.value
  }
}

@Serializable
data class SiteConfig(
  val site: SiteInfo,
  val content: ContentConfig,
  val build: BuildConfig = BuildConfig(),
  val pagination: PaginationConfig = PaginationConfig(),
  val theme: ThemeConfig = ThemeConfig(),
) {
  @kotlinx.serialization.Transient
  var aboutContent: String? = null  // AboutMe 页面的 HTML 内容（运行时设置）

  companion object {
    fun load(configFile: Path = Path("nice.yaml")): SiteConfig {
      // 使用KMP版本的kaml直接解析
      var yamlContent = SystemFileSystem.source(configFile).buffered().use { it.readString() }

      // 环境变量替换（使用 expect/actual 模式）
      yamlContent = replaceEnvVars(yamlContent)
      logger.info { "Loaded site config from $configFile" }
      // 配置 Yaml 以支持 polymorphic types
      val yaml = Yaml(configuration = com.charleskorn.kaml.YamlConfiguration(
        polymorphismStyle = com.charleskorn.kaml.PolymorphismStyle.Property
      ))

      return yaml.decodeFromString(serializer(), yamlContent)
    }
  }
}

@Serializable
data class SiteInfo(
  val title: String,
  val url: String,
  val description: String = "",
  val author: String = "",
  val language: String = "zh-CN",
  val timezone: String = "Asia/Shanghai",
)

@Serializable
data class ContentConfig(
  val source: SourceConfig,
  val feishu: FeishuConfig? = null,
)

@Serializable
@kotlinx.serialization.SerialName("SourceConfig")
@kotlinx.serialization.json.JsonClassDiscriminator("type")
sealed interface SourceConfig {
  @Serializable
  @kotlinx.serialization.SerialName("git")
  data class GitSource(
    val url: String,
    val branch: String = "main",
    val localPath: String = ".cache/content",
  ) : SourceConfig

  @Serializable
  @kotlinx.serialization.SerialName("file")
  data class FileSource(
    val path: String,
  ) : SourceConfig
}

@Serializable
data class FeishuConfig(
  val enabled: Boolean = false,
  val appId: String = "",
  val appSecret: String = "",
)

@Serializable
data class BuildConfig(
  val output: String = "./output",
  val cleanBeforeBuild: Boolean = true,
  val incremental: Boolean = false,
)

@Serializable
data class PaginationConfig(
  val pageSize: Int = 10,
)

@Serializable
data class ThemeConfig(
  val name: String = "simple",
)

