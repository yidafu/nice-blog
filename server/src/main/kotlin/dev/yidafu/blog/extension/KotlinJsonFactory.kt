package dev.yidafu.blog.extension

import io.vertx.core.buffer.Buffer
import io.vertx.core.json.DecodeException
import io.vertx.core.spi.JsonFactory
import io.vertx.core.spi.json.JsonCodec
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.serializer

class KotlinJsonCodec : JsonCodec {
  private val json = Json {
    encodeDefaults = true
    ignoreUnknownKeys = true
  }
  override fun toString(obj: Any, pretty: Boolean): String {
    val element =  json.encodeToJsonElement(obj)

    return element.toString()
  }

  /**
   * https://stackoverflow.com/a/77866934
   */
  override fun < T : Any?> fromString(jsonStr: String, clazz: Class<T>): T {
    val serializer: KSerializer<Any> = serializer(clazz)
    return json.decodeFromString(serializer, jsonStr) as T
  }

  override fun <T : Any?> fromBuffer(jsonStr: Buffer, clazz: Class<T>): T {
    return fromString(jsonStr.toString(), clazz)
  }

  override fun <T : Any?> fromValue(jsonStr: Any?, toValueType: Class<T>?): T {
    throw DecodeException("Kotlin JsonMapping Mapping " + toValueType!!.name + "  is not available")
  }

  override fun toBuffer(obj: Any, pretty: Boolean): Buffer {
    return Buffer.buffer(toString(obj, pretty))
  }

}
class KotlinJsonFactory : JsonFactory {
  private val codec: JsonCodec by lazy {
    KotlinJsonCodec()
  }
  override fun codec(): JsonCodec {
    return  codec
  }
}
