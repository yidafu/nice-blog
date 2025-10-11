plugins {
  alias(libs.plugins.kotlin.jvm)

  alias(libs.plugins.ksp)
  alias(libs.plugins.ktlint)
}

group = "dev.yidafu.blog"
version = "1.0.0-SNAPSHOT"

repositories {
  mavenCentral()
}

dependencies {
//  implementation("com.google.dagger:dagger-compiler:2.51.1")
//  ksp("com.google.dagger:dagger-compiler:2.51.1")
  implementation(libs.symbol.processing.api)
  implementation("com.squareup:kotlinpoet-ksp:2.2.0")
  implementation("com.squareup:kotlinpoet-jvm:2.2.0")
  implementation("org.jetbrains:annotations:13.0")
  implementation(project(":common"))
  testImplementation(libs.kotlin.test)
}

tasks.test {
  useJUnitPlatform()
}
kotlin {
  jvmToolchain(17)
}
