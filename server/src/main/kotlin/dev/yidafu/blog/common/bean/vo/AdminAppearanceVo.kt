package dev.yidafu.blog.common.bean.vo

import de.comahe.i18n4k.Locale

class AdminAppearanceVo(
  locale: Locale,
  currentPath: String,
  siteTitle: String,
  githubUrl: String,
) : AdminBaseVo(
  locale,
  currentPath,
  siteTitle,
  githubUrl
)
