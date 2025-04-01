package dev.yidafu.blog.common

object Routes {
  const val ROOT_URL = "/"
  const val LOGIN_URL = "/login"
  const val LOGOUT_URL = "/logout"
  const val ARTICLE_LIST = "/articles"
  const val ARTICLE_DETAIL = "/articles/:identifier"

  const val UPLOAD_URL = "/upload/*"
  const val PUBLIC_URL = "/public/*"

  const val ADMIN_URL = "/admin"
  const val ADMIN_DASHBOARD_URL = "/admin/dashboard"
  const val CONFIGURATION_URL = "/admin/config"
  const val CONFIG_APPEARANCE_URL = "/admin/config/appearance"
  const val CONFIG_SYNC_URL = "/admin/config/sync"
  const val CONFIG_DATA_SOURCE_URL = "/admin/config/data-source"
  const val PICTURES_URL = "/admin/pictures"

  const val SYNC_URL = "/admin/sync"
  const val SYNC_LOG_URL = "/admin/sync/log"
  const val SYNC_OPERATE_URL = "/admin/sync/operate"
  const val SYNC_API_START_URL = "/api/sync/start"
  const val SYNC_API_LOG_URL = "/api/sync/log/:uuid"
  const val SYNC_LOG_DETAIL_URL = "/admin/sync/log/detail"

  const val ADMIN_ARTICLE_LIST = "/admin/articles"
  const val ADMIN_ARTICLE_DETAIL = "/admin/articles/:id"
  const val ADMIN_ARTICLE_DETAIL_2 = "/admin/articles/:id/detail"
  const val ADMIN_ARTICLE_HISTORY = "/admin/articles/:id/history"
  const val ADMIN_ARTICLE_STATISTIC = "/admin/articles/:id/statistic"
}
