plugins {
  alias(libs.plugins.kotlin.jvm) apply false
  alias(libs.plugins.kotlin.multiplatform) apply false
  alias(libs.plugins.kotlin.serialization) apply false
  alias(libs.plugins.ktlint) apply false
  alias(libs.plugins.ksp) apply false
  alias(libs.plugins.kotlin.kapt) apply false
}

buildscript {
  repositories {
    mavenCentral()
    gradlePluginPortal()
    google()
    maven {
      name = "华为开源镜像库"
      setUrl("https://mirrors.huaweicloud.com/repository/maven/")
    }
  }
  dependencies {
//    classpath ("com.guardsquare:proguard-gradle:7.6.1")
  }
}

allprojects {
  repositories {
    // Required to download KtLint
    gradlePluginPortal()
    mavenCentral()
    maven {
      name = "华为开源镜像库"
      setUrl("https://mirrors.huaweicloud.com/repository/maven/")
    }
    maven("https://s01.oss.sonatype.org/content/repositories/releases/")
    google()
    maven("https://jitpack.io")
  }
}
