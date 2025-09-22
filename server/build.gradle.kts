import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.api.tasks.testing.logging.TestLogEvent.*
plugins {
  alias(libs.plugins.kotlin.jvm)
  alias(libs.plugins.kotlin.kapt)
  application
  alias(libs.plugins.shadow)
  alias(libs.plugins.ksp)
  alias(libs.plugins.kotlin.serialization)
  alias(libs.plugins.ktlint)
  //  id("de.comahe.i18n4k") version "0.9.0"
  id("org.graalvm.buildtools.native") version "0.10.5"
}

group = "dev.yidafu.blog"
version = "1.0.0-SNAPSHOT"

val mainVerticleName = "dev.yidafu.blog.MainVerticle"
val launcherClassName = "io.vertx.core.Launcher"

val watchForChange = "src/**/*"
val doOnChange = "$projectDir/gradlew classes"

application {
  mainClass.set(launcherClassName)
}

dependencies {
  implementation(project(":common"))
  implementation(project(":themes"))
  implementation(libs.vertx.web)
  implementation(libs.vertx.core)
  implementation(libs.vertx.jdbc.client)
  implementation(libs.h2)
  implementation(libs.vertx.lang.kotlin.coroutines)

  implementation(libs.kotlinx.coroutines.core.jvm)
  implementation(libs.kotlinx.serialization.core.jvm)
  implementation(libs.kotlinx.serialization.json.jvm)

  testImplementation(libs.vertx.junit5)
  testImplementation(libs.junit.jupiter)

  implementation(libs.kgit)

  implementation(platform("io.insert-koin:koin-bom:4.0.0"))
  implementation(libs.koin.annotations)
  ksp(libs.koin.ksp.compiler)
  implementation(libs.koin.core)

  implementation(libs.slf4j.api)
  implementation(libs.logback.classic)
  implementation(libs.slf4j.api)

  implementation(libs.kotlinx.html.jvm)
  implementation(libs.kotlin.css.jvm)

  implementation(libs.i18n4k.core.jvm)
  implementation(libs.kotlinwind.css)

  implementation(libs.mapstruct)
  kapt(libs.mapstruct.processor)

  implementation(libs.quartz)

  implementation(libs.jetbrains.markdown)

  implementation(libs.cryptography.core.jvm)
  implementation(libs.cryptography.provider.jdk)

  implementation(libs.jetbrains.markdown)
  implementation(libs.kgit)
  implementation(libs.kaml)
  implementation(libs.kotlinx.serialization.core)
  implementation(libs.kotlinx.serialization.json)

  implementation(libs.kotlinx.html)
  implementation(libs.kotlin.css)
  implementation(libs.jupyter.notebooks.parser)

  implementation(libs.highlights)

  compileOnly(project(":ksp-plugin"))
  ksp(project(":ksp-plugin"))

  // Exposed database library

  implementation(libs.exposed.core)
  implementation(libs.exposed.jdbc)
  implementation(libs.exposed.dao) // Optional
  implementation(libs.exposed.kotlin.datetime)
  implementation(libs.kotlinx.datetime)

  testImplementation(libs.kotest.runner.junit5)
  testImplementation(libs.kotest.assertions.core)
  testImplementation(libs.kotest.property)
}

kapt {
  arguments {
    // Set Mapstruct Configuration options here
    // https://kotlinlang.org/docs/reference/kapt.html#annotation-processor-arguments
    // https://mapstruct.org/documentation/stable/reference/html/#configuration-options
    arg("mapstruct.verbose", "true")
  }
}

kotlin {
  jvmToolchain(17)
}

graalvmNative {
  binaries {
    named("main") {
      // 配置构建选项
      buildArgs.add("--no-fallback")
      buildArgs.add("--enable-http")
      buildArgs.add("--enable-https")
      buildArgs.add("--allow-incomplete-classpath")
      buildArgs.add("--report-unsupported-elements-at-runtime")
      buildArgs.add("-H:+DashboardAll")
      buildArgs.add("--initialize-at-run-time=io.netty.handler.codec.compression.ZstdOptions")

      // 配置反射、资源和JNI配置文件
      buildArgs.add("-H:ReflectionConfigurationResources=/reflect-config.json")
      buildArgs.add("-H:JNIConfigurationResources=/jni-config.json")
      buildArgs.add("-H:ResourceConfigurationResources=/resource-config.json")

      // 优化配置
      buildArgs.add("-O2")

      // 构建报告
      buildArgs.add("--emit-build-report")
    }
  }

  // 配置测试二进制文件
  testSupport = true
}
tasks.withType<ShadowJar> {
  archiveClassifier.set("fat")
  manifest {
    attributes(mapOf("Main-Verticle" to mainVerticleName))
  }
  exclude("**/io/netty/handler/codec/http2/**")
  mergeServiceFiles()
  from("src/main/resources") {
    include("**/*.xml")
  }
}

tasks.withType<Test> {
  useJUnitPlatform()
  testLogging {
    events = setOf(PASSED, SKIPPED, FAILED)
  }
}

tasks.withType<JavaExec> {
  args =
    listOf(
      "run",
      mainVerticleName,
      "--redeploy=$watchForChange",
      "--launcher-class=$launcherClassName",
      "--on-redeploy=$doOnChange",
    )
}

// i18n4k {
//  commentLocale = "zh-CN"
//  sourceCodeLocales = listOf("en", "zh-CN")
// }

configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
}
