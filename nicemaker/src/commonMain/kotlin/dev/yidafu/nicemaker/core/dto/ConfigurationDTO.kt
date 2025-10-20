package dev.yidafu.nicemaker.core.dto

import kotlinx.serialization.Serializable

@Serializable
data class ConfigurationDTO(val configKey: String, val configValue: String)
