package dev.yidafu.blog.themes

import de.comahe.i18n4k.Locale
import dev.yidafu.blog.common.modal.SyncTaskStatus
import dev.yidafu.blog.common.vo.*
import kotlinx.serialization.json.*
import java.time.LocalDateTime

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
      getValueAsString(COMMON_LOCALE),
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
      sourceBranch = "",
    )
  }

inline val DataModal.synchronousConfig: AdminSynchronousVO
  get() {
    return getValue(DataModal.VO_DATA)?.let {
      Json.decodeFromJsonElement<AdminSynchronousVO>(it)
    } ?: AdminSynchronousVO("")
  }

internal val DataModal.syncLogPage: PaginationVO<SyncTaskVO>
  get() {
    return getValue(DataModal.VO_DATA)?.let {
      Json.decodeFromJsonElement<PaginationVO<SyncTaskVO>>(it)
    } ?: PaginationVO()
  }

internal val DataModal.syncTask: SyncTaskVO
  get() {
    return getValue(DataModal.VO_DATA)?.let {
      Json.decodeFromJsonElement<SyncTaskVO>(it)
    } ?: SyncTaskVO(
      id = 0L,
      callbackUrl = "",
      uuid = "",
      status = SyncTaskStatus.Failed,
      createdAt = LocalDateTime.now(),
      logs = "",
    )
  }

internal val DataModal.loginVo: AdminLoginVO
  get() {
    return getValue(DataModal.VO_DATA)?.let {
      Json.decodeFromJsonElement<AdminLoginVO>(it)
    } ?: AdminLoginVO(
      publicKey = "",
      errorMessage = null,
    )
  }

internal val DataModal.dashboardData: DashboardVO
  get() {
    return getValue(DataModal.VO_DATA)?.let {
      Json.decodeFromJsonElement<DashboardVO>(it)
    } ?: DashboardVO(
      articleCount = 0L,
      accessCount = 0L,
    )
  }
