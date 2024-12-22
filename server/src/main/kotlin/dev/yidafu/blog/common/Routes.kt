package dev.yidafu.blog.common

object Routes {
  internal const val ADMIN_URL = "/admin"
  internal const val CONFIGURATION_URL = "/admin/config"
  internal const val CONFIG_APPEARANCE_URL = "/admin/config/appearance"
  internal const val CONFIG_SYNC_URL = "/admin/config/sync"
  internal const val CONFIG_DATA_SOURCE_URL = "/admin/config/data-source"
  internal const val PICTURES_URL = "/admin/pictures"

  internal const val SYNC_URL = "/admin/sync"
  internal const val SYNC_LOG_URL = "/admin/sync/log"
  internal const val SYNC_OPERATE_URL = "/admin/sync/operate"
  internal const val SYNC_API_START_URL = "/api/sync/start"
  internal const val SYNC_API_LOG_URL = "/api/sync/log/:uuid"
  internal const val SYNC_LOG_DETAIL_URL = "/admin/sync/log/detail"

  internal const val LOGIN_URL = "/login"
  internal const val LOGOUT_URL = "/logout"

  internal const val UPLOAD_URL = "/upload/*"
  internal const val PUBLIC_URL = "/public/*"

  internal const val ARTICLE_LIST = "/articles"
  internal const val ARTICLE_DETAIL = "/articles/:identifier"
}
