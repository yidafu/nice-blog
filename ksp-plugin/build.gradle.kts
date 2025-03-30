plugins {
  kotlin("jvm") version "2.0.21"

  id("com.google.devtools.ksp") version "2.0.21-1.0.27"
  id("org.jlleitschuh.gradle.ktlint") version "12.2.0"
}

group = "dev.yidafu.blog"
version = "1.0.0-SNAPSHOT"

repositories {
  mavenCentral()
}

dependencies {
//  implementation("com.google.dagger:dagger-compiler:2.51.1")
//  ksp("com.google.dagger:dagger-compiler:2.51.1")
  implementation("com.google.devtools.ksp:symbol-processing-api:2.0.21-1.0.27")
  implementation("com.squareup:kotlinpoet-ksp:2.1.0")

  testImplementation(kotlin("test"))

}

tasks.test {
  useJUnitPlatform()
}
kotlin {
  jvmToolchain(21)
}
