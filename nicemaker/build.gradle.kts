plugins {
  kotlin("multiplatform") version "2.2.0"
  kotlin("plugin.serialization") version "2.2.0"
}

repositories {
  mavenCentral()
  gradlePluginPortal()
  google()
  maven {
    name = "华为开源镜像库"
    setUrl("https://mirrors.huaweicloud.com/repository/maven/")
  }
  maven {
    setUrl("https://s01.oss.sonatype.org/content/repositories/releases/")
  }
  maven {
    setUrl("https://jitpack.io")
    content {
      includeGroup("com.github.pgreze")
    }
  }
}

group = "dev.yidafu.nicemaker"
version = "0.1.0"

kotlin {
  jvm {
    // 配置 main 二进制文件
    @OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
    mainRun {
      mainClass.set("dev.yidafu.nicemaker.cli.GeneratorCLIKt")
    }
  }

  js(IR) {
    nodejs()
  }

  linuxX64()
  macosX64()
  macosArm64()
  mingwX64()

  sourceSets {
    // 公共代码（所有平台）
    commonMain {
      dependencies {
        // 序列化
        implementation(libs.kotlinx.serialization.core)
        implementation(libs.kotlinx.serialization.json)

        // 协程
        implementation(libs.kotlinx.coroutines.core)

        // 日期时间
        implementation(libs.kotlinx.datetime)

        // IO
        implementation(libs.kotlinx.io.core)

        // HTML
        implementation(libs.kotlinx.html)

        // 日志（KMP 版本）
        implementation(libs.kotlin.logging)

        // 国际化（KMP 版本）
        implementation(libs.i18n4k.core)

        // URI（KMP 版本）
        implementation(libs.uri.kmp)

        // 环境变量（KMP 版本）
        implementation("dev.scottpierce:kotlin-env-var:1.0.6")

        // Markdown（KMP 版本 0.7.3+）
        implementation(libs.jetbrains.markdown)

        // YAML（KMP 版本 0.98.0+）
        implementation(libs.kaml)

        // 代码高亮（KMP 版本 1.1.0+）
        implementation(libs.highlights)
      }
    }

    commonTest {
      dependencies {
        implementation(libs.kotlin.test)
      }
    }

    // JVM 平台特定
    jvmMain {
      dependencies {
        // 进程执行（Git 命令）- KMP库
        implementation(libs.kmp.process)

        // Themes 模块（运行时依赖，通过反射加载）
        runtimeOnly(project(":themes"))

        // CLI
        implementation(libs.kotlinx.cli)

        // Ktor（用于 serve 命令）
        implementation(libs.ktor.server.cio)
        implementation(libs.ktor.server.host.common)

        // Notebook（JVM only）
        implementation(libs.jupyter.notebooks.parser)
        implementation(libs.feishu2html)

        // 其他
        implementation(libs.mapstruct)
        implementation("org.jetbrains:annotations:13.0")
      }
    }
  }
}

// KMP 测试配置
tasks.named<Test>("jvmTest") {
  useJUnitPlatform()
}
