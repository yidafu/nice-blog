import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.api.tasks.testing.logging.TestLogEvent.*
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jooq.meta.jaxb.Logging

plugins {
  kotlin("jvm") version "2.0.21"
  kotlin("kapt") version "2.0.21"
  application
  id("com.github.johnrengelman.shadow") version "7.1.2"
  id("com.google.devtools.ksp") version "2.0.21-1.0.27"
  kotlin("plugin.serialization") version "2.0.21"
  id("org.jooq.jooq-codegen-gradle") version "3.19.16"
  id("org.jlleitschuh.gradle.ktlint") version "12.1.1"

  id("de.comahe.i18n4k") version "0.9.0"
}

group = "dev.yidafu.blog"
version = "1.0.0-SNAPSHOT"

val vertxVersion = "4.5.10"
val junitJupiterVersion = "5.9.1"
val koin_version = "4.0.0"
val koin_annotations_version = "2.0.0-Beta1"

val mainVerticleName = "dev.yidafu.blog.MainVerticle"
val launcherClassName = "io.vertx.core.Launcher"

val watchForChange = "src/**/*"
val doOnChange = "$projectDir/gradlew classes"

application {
  mainClass.set(launcherClassName)
}

dependencies {
  implementation(project(":common"))
  implementation(project(":engine"))
  implementation(platform("io.vertx:vertx-stack-depchain:$vertxVersion"))
  implementation("io.vertx:vertx-web-validation")
  implementation("io.vertx:vertx-health-check")
  implementation("io.vertx:vertx-web")
  implementation("io.vertx:vertx-web-openapi")
  implementation("io.vertx:vertx-mysql-client")
  implementation("io.vertx:vertx-jdbc-client")
  // https://mvnrepository.com/artifact/org.xerial/sqlite-jdbc
  implementation("org.xerial:sqlite-jdbc:3.47.1.0")
  implementation("io.vertx:vertx-lang-kotlin-coroutines")
  implementation("io.vertx:vertx-lang-kotlin")
  implementation("io.vertx:vertx-config")
  implementation("org.hibernate.reactive:hibernate-reactive-core:2.4.2.Final")
  implementation("org.hibernate.orm:hibernate-core:6.6.4.Final")

  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")

  implementation(kotlin("stdlib-jdk8"))
  implementation(kotlin("reflect"))

  testImplementation("io.vertx:vertx-junit5")
  testImplementation("org.junit.jupiter:junit-jupiter:$junitJupiterVersion")

  implementation("com.github.sya-ri:kgit:1.1.0")

  implementation("io.insert-koin:koin-annotations:$koin_annotations_version")
  ksp("io.insert-koin:koin-ksp-compiler:$koin_annotations_version")

  implementation(platform("io.insert-koin:koin-bom:$koin_version"))
  implementation("io.insert-koin:koin-core")

  implementation("ch.qos.logback:logback-classic:1.5.6")

  implementation("org.jetbrains.kotlinx:kotlinx-html:0.8.0")
  implementation("org.jetbrains.kotlin-wrappers:kotlin-css:1.0.0-pre.832")

  implementation("de.comahe.i18n4k:i18n4k-core:0.9.0")
  implementation("io.github.allangomes:kotlinwind-css:0.0.3")

  implementation("org.mapstruct:mapstruct:1.6.0")
  kapt("org.mapstruct:mapstruct-processor:1.6.0")

  implementation("org.quartz-scheduler:quartz:2.5.0")

  implementation("org.jetbrains:markdown:0.7.3")

  implementation("dev.whyoleg.cryptography:cryptography-core:0.4.0")
  implementation("dev.whyoleg.cryptography:cryptography-provider-jdk:0.4.0")

  implementation("org.jooq:jooq:3.19.16")
  implementation("org.jooq:jooq-meta-extensions:3.19.17")
  implementation("org.jooq:jooq-meta-extensions-hibernate:3.19.17")

  jooqCodegen("org.jooq:jooq-meta-extensions:3.19.16")
  jooqCodegen("mysql:mysql-connector-java:8.0.33")
  jooqCodegen("org.jooq:jooq-meta-extensions-hibernate:3.19.17")
  jooqCodegen("org.hibernate:hibernate-core-jakarta:5.6.15.Final")

  jooqCodegen(project(":common"))
  jooqCodegen("org.xerial:sqlite-jdbc:3.47.1.0")
}

kapt {
  arguments {
    // Set Mapstruct Configuration options here
    // https://kotlinlang.org/docs/reference/kapt.html#annotation-processor-arguments
    // https://mapstruct.org/documentation/stable/reference/html/#configuration-options
    arg("mapstruct.verbose", true)
  }
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions.jvmTarget = "17"

tasks.withType<ShadowJar> {
  archiveClassifier.set("fat")
  manifest {
    attributes(mapOf("Main-Verticle" to mainVerticleName))
  }
  mergeServiceFiles()
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

i18n4k {
  commentLocale = "zh-CN"
  sourceCodeLocales = listOf("en", "zh-CN")
}

jooq {
  configuration {
    logging = Logging.TRACE
    jdbc {
      driver = "org.sqlite.JDBC"
      url = "jdbc:sqlite:./nice-blog.db"
//      user = "[your database user]"
//      password = "[your database password]"
    }

    generator {
      name = "org.jooq.codegen.KotlinGenerator"
      generate {
        isKotlinSetterJvmNameAnnotationsOnIsPrefix = true
      }
      database {
        name = "org.jooq.meta.extensions.jpa.JPADatabase"

        properties {
          property {
            key = "packages"
            value = "dev.yidafu.blog.common.modal"
          }

          property {
            key = "unqualifiedSchema"
            value = "none"
          }
        }
      }

      target {
        packageName = "dev.yidafu.blog.common.dao"
        directory = "src/main/kotlin"
      }
    }
  }
}
configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
  debug.set(true)
}
