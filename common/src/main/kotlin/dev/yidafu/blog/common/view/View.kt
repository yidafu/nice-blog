package dev.yidafu.blog.common.view

import de.comahe.i18n4k.messages.MessageBundleLocalizedString
import java.util.Locale

abstract class View(
  protected val model: MutableMap<String, String> = mutableMapOf(),
) {
  abstract val local: Locale

  abstract fun render(): String

  /**
   * get value in model
   */
  fun getValue(key: String): String = model[key] ?: ""

  /**
   * i18n work to normal string with localization
   */
  fun MessageBundleLocalizedString.toText(): String = toString(local)
}
