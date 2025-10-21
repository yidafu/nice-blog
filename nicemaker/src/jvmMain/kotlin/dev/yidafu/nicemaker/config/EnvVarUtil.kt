package dev.yidafu.nicemaker.config

/**
 * JVM 平台的环境变量获取实现
 */
actual fun getEnvVar(name: String): String? = System.getenv(name)

