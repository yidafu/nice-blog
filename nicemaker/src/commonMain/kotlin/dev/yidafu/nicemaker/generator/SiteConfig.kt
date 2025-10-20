package dev.yidafu.nicemaker.generator

import com.charleskorn.kaml.Yaml
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.readString
import kotlinx.serialization.Serializable

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
      val yamlContent = SystemFileSystem.source(configFile).buffered().use { it.readString() }

      // 环境变量替换（暂时禁用，等待kotlin-env-var库配置）
      // val regex = Regex("\\$\\{([^}]+)}")
      // val replaced = regex.replace(yamlContent) { match ->
      //   envVar(match.groupValues[1]) ?: match.value
      // }

      return Yaml.default.decodeFromString(serializer(), yamlContent)
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
data class SourceConfig(
  val type: String = "git",
  val url: String,
  val branch: String = "main",
  val localPath: String = ".cache/content",
)

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

