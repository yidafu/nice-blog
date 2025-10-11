import com.google.devtools.ksp.gradle.KspExtension
import org.gradle.api.tasks.testing.logging.TestLogEvent.*

plugins {
  alias(libs.plugins.kotlin.jvm)
  application
  alias(libs.plugins.ksp)
  alias(libs.plugins.kotlin.serialization)
  alias(libs.plugins.ktlint)
  alias(libs.plugins.ktor)
}

group = "dev.yidafu.blog"
version = "1.0.0-SNAPSHOT"

val mainClassName = "dev.yidafu.blog.ApplicationKt"

application {
  mainClass.set(mainClassName)
}
ktor {
  fatJar {
    archiveFileName.set("nice-blog-fat.jar")
  }
}
dependencies {
  implementation(project(":common"))
  implementation(project(":themes"))

  ksp(project(":ksp-plugin"))

  implementation(libs.ktor.server.content.negotiation)
  implementation(libs.ktor.server.core)
  implementation(libs.ktor.serialization.kotlinx.json)
  implementation(libs.ktor.server.html.builder)
  implementation(libs.kotlinx.html)
  implementation(libs.ktor.server.call.logging)
  implementation(libs.ktor.server.rate.limiting)
//    implementation(libs.koin.ktor)
//    implementation(libs.koin.logger.slf4j)
  implementation(libs.h2)
  implementation(libs.ktor.server.htmx)
  implementation(libs.ktor.htmx.html)
  implementation(libs.kotlin.css)
//    implementation(libs.khealth)
  implementation(libs.ktor.server.call.id)
  implementation(libs.ktor.server.host.common)
  implementation(libs.ktor.server.status.pages)
  implementation(libs.ktor.server.request.validation)
  implementation(libs.ktor.server.sessions)
  implementation(libs.ktor.server.auth)
  implementation(libs.ktor.simple.cache)
  implementation(libs.ktor.server.openapi)
  implementation(libs.ktor.server.http.redirect)
  implementation(libs.ktor.server.default.headers)
  implementation(libs.ktor.server.conditional.headers)
  implementation(libs.ktor.server.compression)
  implementation(libs.ktor.server.caching.headers)
  implementation(libs.ktor.server.cio)
  implementation(libs.ktor.server.config.yaml)
  testImplementation(libs.ktor.server.test.host)
  testImplementation(libs.kotlin.test.junit)

  implementation(libs.ktor.server.di)
  implementation(libs.kotlinx.serialization.core.jvm)
  implementation(libs.kotlinx.serialization.json.jvm)

  implementation(libs.slf4j.api)
  implementation(libs.slf4j.simple)

  implementation(libs.i18n4k.core.jvm)
  implementation(libs.kotlinwind.css)

  implementation(libs.mapstruct)

  implementation(libs.quartz)
  implementation(libs.jetbrains.markdown)
  implementation(libs.cryptography.core.jvm)
  implementation(libs.cryptography.provider.jdk)
  implementation(libs.kaml)
  implementation(libs.jupyter.notebooks.parser)
  implementation(libs.highlights)
  implementation(libs.exposed.core)
  implementation(libs.exposed.jdbc)
  implementation(libs.exposed.dao) // Optional
  implementation(libs.exposed.kotlin.datetime)
  implementation(libs.kotlinx.datetime)

  testImplementation(libs.kotest.runner.junit5)
  testImplementation(libs.kotest.assertions.core)
  testImplementation(libs.kotest.property)
  testImplementation(libs.junit.jupiter)
}

// kapt {
//   arguments {
//     // Set Mapstruct Configuration options here
//     // https://kotlinlang.org/docs/reference/kapt.html#annotation-processor-arguments
//     // https://mapstruct.org/documentation/stable/reference/html/#configuration-options
//     arg("mapstruct.verbose", "true")
//   }
// }

kotlin {
  jvmToolchain(17)
}
tasks.withType<com.google.devtools.ksp.gradle.KspTask>().configureEach {
  outputs.upToDateWhen { false } // 强制每次运行
}

tasks.withType<Test> {
  useJUnitPlatform()
  testLogging {
    events = setOf(PASSED, SKIPPED, FAILED)
  }
}

configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
}

// ksp 不支持修改输出目录，改为手动移动
tasks.register("copyKspOutput") {
  dependsOn("kspKotlin")
  doLast {
    copy {
      from("build/generated/ksp/main/kotlin/dev/yidafu/blog/generated")
      into("src/main/kotlin/dev/yidafu/blog/generated")
    }
    delete("build/generated/ksp/main/kotlin")
  }
}

tasks.named("compileKotlin") {
  dependsOn("copyKspOutput")
}

// 清理任务
tasks.register("cleanGenerated") {
  delete("src/main/kotlin/dev/yidafu/blog/generated")
}
tasks.named("clean") {
  dependsOn("cleanGenerated")
}
