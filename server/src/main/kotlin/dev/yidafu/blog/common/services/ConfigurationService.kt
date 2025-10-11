package dev.yidafu.blog.common.services

import dev.yidafu.blog.common.converter.ConfigurationConvertor
import dev.yidafu.blog.common.db.dao.ConfigurationEntity
import dev.yidafu.blog.common.db.tables.ConfigurationTable
import dev.yidafu.blog.common.dto.ConfigurationDTO
import dev.yidafu.blog.common.modal.ConfigurationModal
import dev.yidafu.blog.common.annotation.Service
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.inList
import org.mapstruct.factory.Mappers
import org.slf4j.LoggerFactory

interface ConfigurationService {
  suspend fun getAll(): List<ConfigurationModal>

  suspend fun getByKey(key: String): ConfigurationModal

  suspend fun getByKeys(keys: List<String>): List<ConfigurationModal>

  suspend fun updateConfig(configs: List<ConfigurationDTO>): Boolean
}

@Service
class ConfigurationServiceImpl : ConfigurationService, ExposedBaseService() {
  private val log = LoggerFactory.getLogger(ConfigurationService::class.java)
  private val configConvertor = Mappers.getMapper(ConfigurationConvertor::class.java)

  /**
   * 获取所有配置项
   */
  override suspend fun getAll(): List<ConfigurationModal> =
    runDB {
      configConvertor.toModalList(ConfigurationEntity.all().toList())
    }

  /**
   * 根据键获取配置项
   */
  override suspend fun getByKey(key: String): ConfigurationModal =
    runDB {
      ConfigurationEntity.find { ConfigurationTable.configKey eq key }
        .singleOrNull()?.let {
          configConvertor.toModal(it)
        }
        ?: throw NoSuchElementException("Configuration with key $key not found")
    }

  /**
   * 根据多个键获取配置项列表
   */
  override suspend fun getByKeys(keys: List<String>): List<ConfigurationModal> =
    runDB {
      val list = ConfigurationEntity.find { ConfigurationTable.configKey inList keys }.toList()
      configConvertor.toModalList(list)
    }

  /**
   * 更新配置项
   * TODO: 批量更新
   */
  override suspend fun updateConfig(configs: List<ConfigurationDTO>): Boolean =
    runDB {
      configs.forEach { config ->
        val existing = ConfigurationEntity.find { ConfigurationTable.configKey eq config.configKey }.singleOrNull()

        if (existing != null) {
          // 更新现有配置
          existing.configValue = config.configValue
        } else {
          // 插入新配置
          ConfigurationEntity.new {
            configKey = config.configKey
            configValue = config.configValue
          }
        }
      }
      true
    }
}
