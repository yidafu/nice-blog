plugins {
  kotlin("jvm") version "2.0.21"
  kotlin("plugin.serialization") version "2.0.21"
  id("org.jlleitschuh.gradle.ktlint") version "12.2.0"
}

group = "dev.yidafu.blog"
version = "0.1.0"

dependencies {
  testImplementation(kotlin("test"))
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.7.3")

  implementation("org.jetbrains.kotlinx:kotlinx-html:0.8.0")
  implementation("org.jetbrains.kotlin-wrappers:kotlin-css:1.0.0-pre.832")

  implementation("io.github.allangomes:kotlinwind-css:0.0.3")
  implementation("com.github.nwillc:ksvg:2.2.0")
  implementation("jakarta.persistence:jakarta.persistence-api:3.2.0")

  implementation("de.comahe.i18n4k:i18n4k-core-jvm:0.9.0")
}

tasks.test {
  useJUnitPlatform()
}
kotlin {
  jvmToolchain(21)
}
