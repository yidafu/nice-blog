plugins {

  id("org.jlleitschuh.gradle.ktlint") version "12.1.1"
}

repositories {
  // Required to download KtLint
  mavenCentral()
  google()
  maven {
    name = "华为开源镜像库"
    setUrl("https://mirrors.huaweicloud.com/repository/maven/")
  }
}
