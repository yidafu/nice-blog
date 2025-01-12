package dev.yidafu.blog.common.services

import dev.yidafu.blog.common.converter.ConfigurationConvertor
import dev.yidafu.blog.common.dao.tables.records.BConfigurationRecord
import dev.yidafu.blog.common.dao.tables.references.B_CONFIGURATION
import dev.yidafu.blog.common.dto.ConfigurationDTO
import dev.yidafu.blog.common.modal.ConfigurationModal
import io.vertx.kotlin.coroutines.vertxFuture
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.future.await
import kotlinx.coroutines.withContext
import org.hibernate.reactive.stage.Stage.SessionFactory
import org.jooq.DSLContext
import org.koin.core.annotation.Single
import org.mapstruct.factory.Mappers
import org.slf4j.LoggerFactory

@Single
class ConfigurationService(
  private val context: DSLContext,
) {
  private val log = LoggerFactory.getLogger(ConfigurationService::class.java)
  private val configConvertor = Mappers.getMapper(ConfigurationConvertor::class.java)

  suspend fun getAll(): List<ConfigurationModal> = withContext(Dispatchers.IO) {
    val configList: Array<BConfigurationRecord> =
      context.selectFrom(B_CONFIGURATION).fetchArray()

    configConvertor.recordToModal(configList.toList())
  }

  suspend fun getByKey(key: String): ConfigurationModal = withContext(Dispatchers.IO) {
    val record: BConfigurationRecord? = context.selectFrom(B_CONFIGURATION).where(
      B_CONFIGURATION.CONFIGKEY.eq(key)
    ).fetchOne()

    configConvertor.recordToModal(record)
  }

  suspend fun getByKeys(keys: List<String>): List<ConfigurationModal> = withContext(Dispatchers.IO) {
    val list: Array<BConfigurationRecord> = context.selectFrom(B_CONFIGURATION).where(
      B_CONFIGURATION.CONFIGKEY.`in`(keys)
    ).fetchArray()

    configConvertor.recordToModal(list.toList())
  }

  suspend fun updateConfig(configs: List<ConfigurationDTO>): Boolean  = withContext(Dispatchers.IO){
    context.batch(
      configs.map { config ->
        context.update(B_CONFIGURATION)
          .set(B_CONFIGURATION.CONFIGVALUE, config.configValue)
          .where(B_CONFIGURATION.CONFIGKEY.eq(config.configKey))
      }
    ).execute()

    true
  }
}
