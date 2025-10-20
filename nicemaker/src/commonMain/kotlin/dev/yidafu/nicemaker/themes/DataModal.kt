package dev.yidafu.nicemaker.themes

import de.comahe.i18n4k.Locale
import dev.yidafu.nicemaker.common.vo.*
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.json.*
import kotlin.time.ExperimentalTime

class DataModal(
  protected val dataStore: JsonObject = JsonObject(emptyMap()),
) {
  fun getValue(key: String): JsonElement? {
    return dataStore[key]
  }

  private inline fun getValueAsString(key: String): String {
    val value = dataStore[key]
    if (value is JsonPrimitive) {
      return value.content
    }
    throw IllegalStateException("$key is not a string")  // 使用KMP兼容的异常
  }

  val locale: Locale by lazy {
    Locale(getValueAsString(COMMON_LOCALE))
  }

  val path: String = getValueAsString(CURRENT_PATH)

  val siteTitle: String = getValueAsString(SITE_TITLE)

  val githubUrl: String = getValueAsString(GITHUB_URL)

  // 新增的数据访问方法
  fun str(path: String, default: String = ""): String {
    return getByPathPublic(path)?.let {
      when (it) {
        is JsonPrimitive -> it.contentOrNull ?: default
        else -> default
      }
    } ?: default
  }

  fun int(path: String, default: Int = 0): Int {
    return getByPathPublic(path)?.let {
      when (it) {
        is JsonPrimitive -> it.intOrNull ?: default
        else -> default
      }
    } ?: default
  }

  fun long(path: String, default: Long = 0L): Long {
    return getByPathPublic(path)?.let {
      when (it) {
        is JsonPrimitive -> it.longOrNull ?: default
        else -> default
      }
    } ?: default
  }

  fun bool(path: String, default: Boolean = false): Boolean {
    return getByPathPublic(path)?.let {
      when (it) {
        is JsonPrimitive -> it.booleanOrNull ?: default
        else -> default
      }
    } ?: default
  }

  inline fun <reified T> list(path: String): List<T> {
    return getByPathPublic(path)?.let {
      try {
        Json.decodeFromJsonElement<List<T>>(it)
      } catch (e: Exception) {
        emptyList()
      }
    } ?: emptyList()
  }

  inline fun <reified T> obj(path: String): T? {
    return getByPathPublic(path)?.let {
      try {
        Json.decodeFromJsonElement<T>(it)
      } catch (e: Exception) {
        null
      }
    }
  }

  fun getByPathPublic(path: String): JsonElement? {
    val parts = path.split(".")
    var current: JsonElement? = dataStore[VO_DATA]

    for (part in parts) {
      current = when (current) {
        is JsonObject -> current[part]
        else -> return null
      }
    }

    return current
  }

  companion object {
    const val COMMON_LOCALE = "locale"
    const val CURRENT_PATH = "currentPath"
    const val SITE_TITLE = "siteTitle"
    const val GITHUB_URL = "githubUrl"

    const val VO_DATA = "voData"
  }
}

inline val DataModal.articleList: List<ArticleVO>
  get() {
    return getValue(DataModal.VO_DATA)?.let {
      Json.decodeFromJsonElement<List<ArticleVO>>(it)
    } ?: emptyList()
  }
inline val DataModal.articleDetail: ArticleVO
  get() {
    return getValue(DataModal.VO_DATA)?.let {
      Json.decodeFromJsonElement<ArticleVO>(it)
    } ?: ArticleVO()
  }

inline val DataModal.articlePage: PaginationVO<ArticleVO>
  get() {
    return getValue(DataModal.VO_DATA)?.let {
      Json.decodeFromJsonElement<PaginationVO<ArticleVO>>(it)
    } ?: PaginationVO()
  }

