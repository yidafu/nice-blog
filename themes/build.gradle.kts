plugins {
  alias(libs.plugins.kotlin.jvm)
  alias(libs.plugins.kotlin.serialization)
  alias(libs.plugins.ktlint)
  alias(libs.plugins.ksp)
  alias(libs.plugins.i18n4k)
}

group = "dev.yidafu.blog"
version = "0.1.0"

repositories {
  mavenCentral()
}

dependencies {
  testImplementation(libs.kotlin.test)
  implementation(libs.kotlinx.serialization.core)
  implementation(libs.kotlinx.serialization.json)
  implementation(libs.kotlinx.html)
  implementation(libs.ktor.server.html.builder)
  implementation(libs.kotlin.css)
  implementation(libs.ksvg)
  implementation(libs.i18n4k.core.jvm)
  ksp(libs.auto.service.ksp)
  implementation(libs.auto.service.annotations)
  implementation(project(":common"))

  testImplementation(libs.kotest.runner.junit5)
  testImplementation(libs.kotest.assertions.core)
  testImplementation(libs.kotest.property)
  implementation(libs.kotlinx.datetime)
}

tasks.test {
  useJUnitPlatform()
}
kotlin {
  jvmToolchain(17)
}

ksp {
  arg("autoserviceKsp.verify", "true")
  arg("autoserviceKsp.verbose", "true")
}

i18n4k {
  commentLocale = "zh-CN"
  sourceCodeLocales = listOf("en", "zh-CN")
}
