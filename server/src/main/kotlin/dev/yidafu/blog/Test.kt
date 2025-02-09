package dev.yidafu.blog

import dev.whyoleg.cryptography.CryptographyProvider
import dev.whyoleg.cryptography.DelicateCryptographyApi
import dev.whyoleg.cryptography.algorithms.MD5
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@OptIn(ExperimentalEncodingApi::class, DelicateCryptographyApi::class)
suspend fun main() {
  val provider = CryptographyProvider.Default
  val md5 = provider.get(MD5)

  println(Base64.encode(md5.hasher().hash("admin".toByteArray())))
}
