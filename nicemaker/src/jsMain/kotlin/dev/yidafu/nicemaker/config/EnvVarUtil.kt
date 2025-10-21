package dev.yidafu.nicemaker.config

/**
 * JS/Node.js 平台的环境变量获取实现
 */
actual fun getEnvVar(name: String): String? {
  return try {
    // 访问 Node.js 的 process.env
    val envValue = js("process.env[name]")
    if (envValue == null || envValue == undefined) null else envValue as String
  } catch (e: Exception) {
    null
  }
}

