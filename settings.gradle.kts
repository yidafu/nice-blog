plugins {
  id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "nice-blog"

include("server")
include("common")
include("ksp-plugin")
include("themes")
