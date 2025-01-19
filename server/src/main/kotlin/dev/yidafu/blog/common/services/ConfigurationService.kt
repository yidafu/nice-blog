package dev.yidafu.blog.common.services

import dev.yidafu.blog.common.converter.ConfigurationConvertor
import dev.yidafu.blog.common.dao.tables.records.BConfigurationRecord
import dev.yidafu.blog.common.dao.tables.references.B_CONFIGURATION
import dev.yidafu.blog.common.dto.ConfigurationDTO
import dev.yidafu.blog.common.modal.ConfigurationModal
import org.jooq.CloseableDSLContext
import org.koin.core.annotation.Single
import org.mapstruct.factory.Mappers
import org.slf4j.LoggerFactory

@Single
class ConfigurationService(
  private val context: CloseableDSLContext,
) : BaseService(context) {
  private val log = LoggerFactory.getLogger(ConfigurationService::class.java)
  private val configConvertor = Mappers.getMapper(ConfigurationConvertor::class.java)

  suspend fun getAll(): List<ConfigurationModal> =
    runDB {
      val configList: Array<BConfigurationRecord> =
        context.selectFrom(B_CONFIGURATION).fetchArray()

      configConvertor.recordToModal(configList.toList())
    }

  suspend fun getByKey(key: String): ConfigurationModal =
    runDB {
      val record: BConfigurationRecord? =
        context.selectFrom(B_CONFIGURATION).where(
          B_CONFIGURATION.CONFIG_KEY.eq(key),
        ).fetchOne()

      configConvertor.recordToModal(record)
    }

  suspend fun getByKeys(keys: List<String>): List<ConfigurationModal> =
    runDB {
      val list: Array<BConfigurationRecord> =
        context.selectFrom(B_CONFIGURATION).where(
          B_CONFIGURATION.CONFIG_KEY.`in`(keys),
        ).fetchArray()

      configConvertor.recordToModal(list.toList())
    }

  suspend fun updateConfig(configs: List<ConfigurationDTO>): Boolean =
    runDB {
      context.batch(
        configs.map { config ->
          context.update(B_CONFIGURATION)
            .set(B_CONFIGURATION.CONFIG_VALUE, config.configValue)
            .where(B_CONFIGURATION.CONFIG_KEY.eq(config.configKey))
        },
      ).execute()

      true
    }
}
