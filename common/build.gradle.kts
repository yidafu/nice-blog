plugins {
  alias(libs.plugins.kotlin.jvm)
  alias(libs.plugins.kotlin.serialization)
  alias(libs.plugins.ktlint)
  alias(libs.plugins.ksp)
}
repositories {
  mavenCentral()
  gradlePluginPortal()
  google()
  maven {
    name = "华为开源镜像库"
    setUrl("https://mirrors.huaweicloud.com/repository/maven/")
  }
}
group = "dev.yidafu.blog"
version = "0.1.0"

dependencies {
  testImplementation(libs.kotlin.test)
  implementation(libs.kotlinx.serialization.core)
  implementation(libs.kotlinx.serialization.json)
  implementation(libs.kotlinx.serialization.core.jvm)
  implementation(libs.kotlinx.serialization.json.jvm)

  implementation(libs.kotlinx.html)
  implementation(libs.kotlin.css)

//  implementation(libs.kotlinwind.css)
//  implementation(libs.ksvg)
  implementation(libs.jakarta.persistence.api)

  implementation(libs.ktor.server.core)
  implementation(libs.ktor.serialization.kotlinx.json)
//  implementation(libs.i18n4k.core.jvm)
//  ksp(libs.auto.service.ksp)
//  implementation(libs.auto.service.ksp)
//  implementation(libs.auto.service.annotations)
  implementation(libs.kotlinx.datetime)
  implementation(libs.kotlinx.datetime.jvm)
  implementation("org.jetbrains:annotations:13.0")
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
