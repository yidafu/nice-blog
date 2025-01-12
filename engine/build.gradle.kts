plugins {
  kotlin("jvm") version "2.0.21"
  kotlin("plugin.serialization") version "2.0.21"
  id("org.jlleitschuh.gradle.ktlint") version "12.1.1"
}

group = "dev.yidafu.blog"
version = "0.1.0"

dependencies {
  implementation(project(":common"))
  testImplementation(kotlin("test"))

  implementation("org.jetbrains:markdown:0.7.3")
  implementation("com.github.sya-ri:kgit:1.1.0")

  implementation("ch.qos.logback:logback-classic:1.5.6")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")

  implementation("com.charleskorn.kaml:kaml:0.66.0")
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.7.3")
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")

  implementation("org.jetbrains.kotlinx:kotlinx-html:0.8.0")
  implementation("org.jetbrains.kotlin-wrappers:kotlin-css:1.0.0-pre.832")

  implementation("dev.snipme:highlights:1.0.0")
}

tasks.test {
  useJUnitPlatform()
}
kotlin {
  jvmToolchain(17)
}
