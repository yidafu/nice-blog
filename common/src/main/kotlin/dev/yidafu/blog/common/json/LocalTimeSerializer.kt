package dev.yidafu.blog.common.json

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Serializer(forClass = LocalTime::class)
object LocalTimeSerializer : KSerializer<LocalTime> {
  private val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")

  override val descriptor: SerialDescriptor
    get() = PrimitiveSerialDescriptor("LocalTime", PrimitiveKind.STRING)

  override fun serialize(encoder: Encoder, value: LocalTime) {
    encoder.encodeString(value.format(formatter))
  }

  override fun deserialize(decoder: Decoder): LocalTime {
    return LocalTime.parse(decoder.decodeString(), formatter)
  }
}
