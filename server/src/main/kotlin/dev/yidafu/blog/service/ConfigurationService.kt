package dev.yidafu.blog.service

import dev.yidafu.blog.bean.converter.ConfigurationConvertor
import dev.yidafu.blog.bean.dto.ConfigurationDTO
import dev.yidafu.blog.modal.ConfigurationModal
import io.vertx.kotlin.coroutines.vertxFuture
import kotlinx.coroutines.future.await
import org.hibernate.reactive.stage.Stage.SessionFactory
import org.koin.core.annotation.Single
import org.mapstruct.factory.Mappers
import org.slf4j.LoggerFactory

@Single
class ConfigurationService(
  private val sessionFactory: SessionFactory,
) {
  private val log = LoggerFactory.getLogger(ConfigurationService::class.java)
  private val configConvertor = Mappers.getMapper(ConfigurationConvertor::class.java)

  suspend fun getAll(): List<ConfigurationModal> {
    val criteriaBuilder = sessionFactory.criteriaBuilder
    return sessionFactory.withSession { session ->
      val query = criteriaBuilder.createQuery(ConfigurationModal::class.java)
      val from = query.from(ConfigurationModal::class.java)
      query.select(from)
      session.createQuery(query).resultList
    }.await()
  }

  suspend fun getByKey(key: String): ConfigurationModal {
    val criteriaBuilder = sessionFactory.criteriaBuilder
    return sessionFactory.withSession { session ->
      val query = criteriaBuilder.createQuery(ConfigurationModal::class.java)
      val from = query.from(ConfigurationModal::class.java)
      query.where(criteriaBuilder.equal(from.get<String>(ConfigurationModal::configKey.name), key))
      query.select(from)
      session.createQuery(query).singleResult
    }.await()
  }

  suspend fun getByKeys(keys: List<String>): List<ConfigurationModal> {
    val criteriaBuilder = sessionFactory.criteriaBuilder
    return sessionFactory.withSession { session ->
      val query = criteriaBuilder.createQuery(ConfigurationModal::class.java)
      val from = query.from(ConfigurationModal::class.java)
      query.where(
        from.get<Boolean?>(ConfigurationModal::configKey.name).`in`(keys)
      )
      query.select(from)
      session.createQuery(query).resultList
    }.await()
  }

  suspend fun updateConfig(configs: List<ConfigurationDTO>): Boolean {
//    val modals = configConvertor.toModal(configs)
    val builder = sessionFactory.criteriaBuilder
    sessionFactory.withSession { session ->
      vertxFuture {
        configs.map { config ->
          log.info("update config $config")
          val update = builder.createCriteriaUpdate(ConfigurationModal::class.java)
          val from = update.from(ConfigurationModal::class.java)
          update.set(ConfigurationModal::configValue.name, config.configValue)
          update.where(builder.equal(from.get<String>(ConfigurationModal::configKey.name), config.configKey))
          session.createQuery(update).executeUpdate().await()
        }
        session.flush().await()
      }.toCompletionStage()
    }?.await()

    return true
  }
}
