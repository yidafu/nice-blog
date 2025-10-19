plugins {
  alias(libs.plugins.kotlin.jvm)
  alias(libs.plugins.kotlin.serialization)
  alias(libs.plugins.ktlint)
  alias(libs.plugins.ksp)
  alias(libs.plugins.kotlin.kapt)
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
