package dev.yidafu.blog.common

import java.math.BigInteger
import java.util.concurrent.ThreadLocalRandom

object ShortUUID {
  const val BASE62 = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"

  @Synchronized
  @JvmStatic
  fun generate(): String {
    val timestamp = System.currentTimeMillis()

    // 时间戳部分（8字符）
    val timePart = base62Encode(timestamp, 8).take(8)

    // 随机数部分（8字符）
    val random = ThreadLocalRandom.current()
    val randomPart = base62Encode(random.nextLong(), 8).take(8)

    return timePart + randomPart
  }

  private fun base62Encode(
    value: Long,
    minLength: Int,
  ): String {
    // 处理无符号数值
//    val unsignedValue = BigInteger(1, ByteBuffer.allocate(8).putLong(value).array())
    val sixtyTwo = BigInteger.valueOf(62)
    val sb = StringBuilder()
    var v = if (value < 0) (value and Long.MAX_VALUE) else value
    do {
      val rem = v % 62
      sb.insert(0, BASE62[rem.toInt()])
      v /= 62
    } while (v > 0 || sb.length < minLength)

    // 补齐到最小长度
    while (sb.length < minLength) {
      sb.insert(0, BASE62[0])
    }

    return sb.toString()
  }
}
