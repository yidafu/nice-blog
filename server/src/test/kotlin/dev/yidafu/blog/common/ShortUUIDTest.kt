package dev.yidafu.blog.common


import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldBeUnique
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldHaveLength
import io.kotest.matchers.string.shouldMatch

class ShortUUIDTest : StringSpec({
  "generated UUID should have a length of 16" {
    val uuid = ShortUUID.generate()
    uuid shouldHaveLength 16
  }

  "generated UUID should contain only base62 characters" {
    val uuid = ShortUUID.generate()
    uuid.shouldMatch(Regex("[a-zA-Z0-9]{16}"))
  }

  "generated UUIDs should be unique" {
    val uuidSet = mutableSetOf<String>()
    val count = 1000
    repeat(count) {
      val uuid = ShortUUID.generate()
      uuidSet.add(uuid)
    }
    uuidSet.size shouldBe count
    uuidSet.shouldBeUnique()
  }
})
