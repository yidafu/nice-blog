plugins {
  alias(libs.plugins.kotlin.jvm)
  alias(libs.plugins.kotlin.serialization)
  alias(libs.plugins.ktlint)
  alias(libs.plugins.ksp)
}

group = "dev.yidafu.blog"
version = "0.1.0"

dependencies {
  testImplementation(libs.kotlin.test)
  implementation(libs.kotlinx.serialization.core)
  implementation(libs.kotlinx.serialization.json)

  implementation(libs.kotlinx.html)
  implementation(libs.kotlin.css)

//  implementation(libs.kotlinwind.css)
//  implementation(libs.ksvg)
  implementation(libs.jakarta.persistence.api)

//  implementation(libs.i18n4k.core.jvm)
//  ksp(libs.auto.service.ksp)
//  implementation(libs.auto.service.ksp)
//  implementation(libs.auto.service.annotations)
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
