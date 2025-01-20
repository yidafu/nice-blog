package dev.yidafu.blog.common.bean.bo

import dev.whyoleg.cryptography.algorithms.RSA

class RSAKeyPair(
  val privateKey: RSA.OAEP.PrivateKey,
  val publicKEy: String,
)
