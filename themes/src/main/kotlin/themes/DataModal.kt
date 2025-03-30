package dev.yidafu.blog.themes

import de.comahe.i18n4k.Locale
import dev.yidafu.blog.common.vo.AdminDataSourceVO
import dev.yidafu.blog.common.vo.AdminSynchronousVO
import dev.yidafu.blog.common.vo.ArticleVO
import dev.yidafu.blog.common.vo.PaginationVO
import kotlinx.serialization.json.*

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
    throw IllegalAccessException("$key is not a string")
  }

  val locale: Locale by lazy {
    Locale.forLanguageTag(
      getValueAsString(COMMON_LOCALE)
    )
  }


  val path: String = getValueAsString(CURRENT_PATH)

  val siteTitle: String = getValueAsString(SITE_TITLE)

  val githubUrl: String = getValueAsString(GITHUB_URL)


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

// TODO: cache decode result
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


inline val DataModal.dataSource: AdminDataSourceVO
  get() {
    return getValue(DataModal.VO_DATA)?.let {
      Json.decodeFromJsonElement<AdminDataSourceVO>(it)
    } ?: AdminDataSourceVO(
      sourceType = "",
      sourceUrl = "",
      sourceToken = "",
      sourceBranch = ""
    )
  }




inline val DataModal.synchronousConfig: AdminSynchronousVO
  get() {
    return getValue(DataModal.VO_DATA)?.let {
      Json.decodeFromJsonElement<AdminSynchronousVO>(it)
    } ?: AdminSynchronousVO("")
  }
