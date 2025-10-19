package dev.yidafu.nicemaker.common.json

import dev.yidafu.nicemaker.common.ext.formatString
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format.char
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

val format =
  LocalDateTime.Format {
    year()
    char('-')
    monthNumber()
    char('-')
    dayOfMonth()
    char(' ')
    hour()
    char(':')
    minute()
    char(':')
    second()
  }

@Serializer(forClass = LocalDateTime::class)
class LocalDateTimeSerializer {
  override val descriptor: SerialDescriptor
    get() = PrimitiveSerialDescriptor("LocalDateTime", PrimitiveKind.STRING)

  override fun serialize(
    encoder: Encoder,
    value: LocalDateTime,
  ) {
    encoder.encodeString(value.formatString())
  }

  override fun deserialize(decoder: Decoder): LocalDateTime {
    return LocalDateTime.parse(decoder.decodeString(), format)
  }
}
