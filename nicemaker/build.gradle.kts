plugins {
  kotlin("multiplatform") version "2.2.0"
  kotlin("plugin.serialization") version "2.2.0"
}

repositories {
  mavenLocal()  // 本地Maven仓库，用于jupyter-notebooks-parser
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
    name = "Sonatype Snapshots"
    setUrl("https://s01.oss.sonatype.org/content/repositories/snapshots/")
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

        // Jupyter Notebook解析（KMP库）
        implementation(libs.jupyter.notebooks.parser)

        // Ktor Client（用于 feishu2html）
        implementation(libs.ktor.client.core)
        implementation(libs.ktor.client.cio)
        implementation(libs.ktor.client.content.negotiation)
        implementation(libs.ktor.serialization.kotlinx.json)

        // 飞书文档（KMP 版本）
        implementation(libs.feishu2html)
      }
    }

    commonTest {
      dependencies {
        implementation(libs.kotlin.test)
      }
    }

    // Apple 平台（macOS）
    appleMain {
      dependencies {
        // Apple 平台使用 kmp-process
        implementation(libs.kmp.process)
      }
    }

    // Linux 平台
    linuxMain {
      dependencies {
        // Linux 平台使用 kmp-process
        implementation(libs.kmp.process)
      }
    }

    // Windows 平台特定
    mingwMain {
      dependencies {
        // mingwX64 不使用 kmp-process，使用手动实现
      }
    }

    // JS 平台特定
    jsMain {
      dependencies {
        // JS 平台使用 kmp-process
        implementation(libs.kmp.process)
      }
    }

    // JVM 平台特定
    jvmMain {
      dependencies {
        // JVM 平台使用 kmp-process
        implementation(libs.kmp.process)

        // Themes 模块（运行时依赖，用于主题注册）
        runtimeOnly(project(":themes"))

        // CLI
        implementation(libs.kotlinx.cli)

        // Ktor Server（用于 serve 命令）
        implementation(libs.ktor.server.cio)
        implementation(libs.ktor.server.host.common)

        // Ktor Client（用于 feishu2html - 确保运行时可用）
        implementation(libs.ktor.client.core)
        implementation(libs.ktor.client.cio)
        implementation(libs.ktor.client.content.negotiation)
        implementation(libs.ktor.serialization.kotlinx.json)
      }
    }
  }
}

// KMP 测试配置
tasks.named<Test>("jvmTest") {
  useJUnitPlatform()
}

// 强制所有依赖使用统一版本
configurations.all {
  resolutionStrategy {
    // Ktor 版本统一
    force("io.ktor:ktor-client-core:3.3.0")
    force("io.ktor:ktor-client-cio:3.3.0")
    force("io.ktor:ktor-client-content-negotiation:3.3.0")
    force("io.ktor:ktor-serialization-kotlinx-json:3.3.0")
    force("io.ktor:ktor-http:3.3.0")
    force("io.ktor:ktor-utils:3.3.0")
    force("io.ktor:ktor-io:3.3.0")

    // kotlinx-html 版本统一
    force("org.jetbrains.kotlinx:kotlinx-html:0.11.0")
    force("org.jetbrains.kotlinx:kotlinx-html-jvm:0.11.0")
  }
}

// 配置 jvmRun 任务的工作目录为项目根目录
tasks.withType<JavaExec>().configureEach {
  if (name == "jvmRun") {
    workingDir = rootProject.projectDir
  }
}
