plugins {
  kotlin("multiplatform") version "2.2.0"
  kotlin("plugin.serialization") version "2.2.0"
  id("de.comahe.i18n4k") version "0.11.0"
}

group = "dev.yidafu.nicemaker"
version = "0.1.0"

repositories {
  mavenLocal()  // 本地Maven仓库
  mavenCentral()
}

kotlin {
  jvm()

  js(IR) {
    nodejs()
  }

  linuxX64()
  macosX64()
  macosArm64()
  mingwX64()

  sourceSets {
    commonMain {
      dependencies {
        // 依赖 nicemaker 获取接口定义
        implementation(project(":nicemaker"))

        // HTML
        implementation(libs.kotlinx.html)

        // 序列化
        implementation(libs.kotlinx.serialization.core)
        implementation(libs.kotlinx.serialization.json)

        // 国际化（KMP 版本）
        implementation(libs.i18n4k.core)

        // 日期时间
        implementation(libs.kotlinx.datetime)
      }
    }

    commonTest {
      dependencies {
        implementation(libs.kotlin.test)
      }
    }

    jvmMain {
      dependencies {
        // CSS
        implementation(libs.kotlin.css)
        implementation(libs.ksvg)
      }
    }

    jvmTest {
      dependencies {
        implementation(libs.kotest.runner.junit5)
        implementation(libs.kotest.assertions.core)
        implementation(libs.kotest.property)
      }
    }
  }
}

tasks.named<Test>("jvmTest") {
  useJUnitPlatform()
}

i18n4k {
  commentLocale = "zh-CN"
  sourceCodeLocales = listOf("en", "zh-CN")
}
