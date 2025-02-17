plugins {
  id( "ca.cutterslade.analyze") version "1.9.1"

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

  apply(plugin = "ca.cutterslade.analyze")
}
