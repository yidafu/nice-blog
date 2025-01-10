plugins {
  kotlin("jvm") version "2.0.21"
  kotlin("plugin.serialization") version "2.0.21"

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
// https://mvnrepository.com/artifact/org.hibernate/hibernate-core
  implementation("org.hibernate:hibernate-core:5.6.15.Final")
  // https://mvnrepository.com/artifact/jakarta.persistence/jakarta.persistence-api
  implementation("jakarta.persistence:jakarta.persistence-api:3.2.0")

}

tasks.test {
  useJUnitPlatform()
}
kotlin {
  jvmToolchain(17)
}
