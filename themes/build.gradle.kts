plugins {
  kotlin("jvm") version "2.0.21"
  kotlin("plugin.serialization") version "2.0.21"
  id("org.jlleitschuh.gradle.ktlint") version "12.2.0"
  id("com.google.devtools.ksp") version "2.0.21-1.0.27"
  id("de.comahe.i18n4k") version "0.10.0"
}

group = "dev.yidafu.blog"
version = "0.1.0"

repositories {
  mavenCentral()
}

dependencies {
  testImplementation(kotlin("test"))
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.8.0")
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.0")
  implementation("org.jetbrains.kotlinx:kotlinx-html:0.8.0")
  implementation("org.jetbrains.kotlin-wrappers:kotlin-css:1.0.0-pre.832")
  implementation("io.github.allangomes:kotlinwind-css:0.0.3")
  implementation("com.github.nwillc:ksvg:2.2.0")
  implementation("de.comahe.i18n4k:i18n4k-core-jvm:0.10.0")
  ksp("dev.zacsweers.autoservice:auto-service-ksp:1.2.0")
  implementation("com.google.auto.service:auto-service-annotations:1.1.1")
  implementation(project(":common"))

  testImplementation("io.kotest:kotest-runner-junit5:5.9.0")
  testImplementation("io.kotest:kotest-assertions-core:5.9.0")
  testImplementation("io.kotest:kotest-property:5.9.0")
}


tasks.test {
  useJUnitPlatform()
}
kotlin {
  jvmToolchain(21)
}
ksp {
  arg("autoserviceKsp.verify", "true")
  arg("autoserviceKsp.verbose", "true")
}

i18n4k {
  commentLocale = "zh_CN"
  sourceCodeLocales = listOf("en", "zh_CN")
}
