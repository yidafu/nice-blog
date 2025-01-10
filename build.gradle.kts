plugins {
  id("org.jlleitschuh.gradle.ktlint") version "12.1.1" apply false
}

allprojects {
  repositories {
    // Required to download KtLint
    maven {
      name = "华为开源镜像库"
      setUrl("https://mirrors.huaweicloud.com/repository/maven/")
    }
    mavenCentral()
    google()
    maven("https://jitpack.io")
  }
}
