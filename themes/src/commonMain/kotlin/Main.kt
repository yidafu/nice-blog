package dev.yidafu.nicemaker

import dev.yidafu.nicemaker.i18n.AdminTxt
import java.util.*

fun main() {
  println("lang tag ${Locale.forLanguageTag("zh-CN").toLanguageTag()}")
  println("local object ${Locale.forLanguageTag(Locale.forLanguageTag("zh-CN").toLanguageTag())}")
  println(AdminTxt.status.toString(Locale.forLanguageTag("zh_CN")))
}
