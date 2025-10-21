package dev.yidafu.nicemaker.config

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.toKString
import platform.posix.getenv

/**
 * Native 平台的环境变量获取实现
 */
@OptIn(ExperimentalForeignApi::class)
actual fun getEnvVar(name: String): String? = getenv(name)?.toKString()

