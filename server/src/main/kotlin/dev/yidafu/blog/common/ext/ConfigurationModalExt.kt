package dev.yidafu.blog.common.ext

import dev.yidafu.blog.common.modal.ConfigurationModal

internal fun List<ConfigurationModal>.getByKey(key: String): String? {
  return find { it.configKey == key }?.configValue
}
