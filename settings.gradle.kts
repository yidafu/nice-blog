plugins {
  id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "nicemaker"

// include("server")  // 已归档：动态服务已被静态生成器替代
// include("ksp-plugin")  // 已归档：仅 server 模块使用
// include("common")  // 已合并到 nicemaker
// include("generator")  // 已合并到 nicemaker
// include("cli")  // 已合并到 nicemaker
include("nicemaker")
include("themes")
