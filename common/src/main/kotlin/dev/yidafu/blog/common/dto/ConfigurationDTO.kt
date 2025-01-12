package dev.yidafu.blog.common.dto

import kotlinx.serialization.Serializable

@Serializable
data class ConfigurationDTO(val configKey: String, val configValue: String)
