package dev.yidafu.blog.common.json

import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Serializer(forClass = LocalDateTime::class)
class LocalDateTimeSerializer {
  private val formatter = DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss")

  override val descriptor: SerialDescriptor
    get() = PrimitiveSerialDescriptor("LocalDateTime", PrimitiveKind.STRING)

  override fun serialize(
    encoder: Encoder,
    value: LocalDateTime,
  ) {
    encoder.encodeString(value.format(formatter))
  }

  override fun deserialize(decoder: Decoder): LocalDateTime {
    return LocalDateTime.parse(decoder.decodeString(), formatter)
  }
}
