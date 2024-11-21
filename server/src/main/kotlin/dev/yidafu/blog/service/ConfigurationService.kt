package dev.yidafu.blog.service

import dev.yidafu.blog.bean.converter.ConfigurationMapper
import dev.yidafu.blog.bean.dto.ConfigurationDTO
import dev.yidafu.blog.modal.ConfigurationModal
import io.vertx.kotlin.coroutines.vertxFuture
import kotlinx.coroutines.future.await
import org.hibernate.reactive.stage.Stage.SessionFactory
import org.koin.core.annotation.Single
import org.mapstruct.factory.Mappers
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionStage

@Single
class ConfigurationService(
  private val sessionFactory: SessionFactory,
) {
  private val configConvertor = Mappers.getMapper(ConfigurationMapper::class.java)

  suspend fun getAll(): List<ConfigurationModal> {
    val criteriaBuilder = sessionFactory.criteriaBuilder
    return sessionFactory.withSession { session ->
      val query = criteriaBuilder.createQuery(ConfigurationModal::class.java)
      val from = query.from(ConfigurationModal::class.java)
      query.select(from)
      session.createQuery(query).resultList
    }.await()
  }

  suspend fun updateConfig(configs: List<ConfigurationDTO>): Boolean {
    val modals = configConvertor.toModal(configs)
    val builder = sessionFactory.criteriaBuilder
    sessionFactory.withSession { session ->
      vertxFuture {
        modals.map { modal ->
          val update = builder.createCriteriaUpdate(ConfigurationModal::class.java)
          val from = update.from(ConfigurationModal::class.java)
          update.set(ConfigurationModal::configValue.name, modal.configValue)
        update.where(builder.equal(from.get<String>(ConfigurationModal::configKey.name), modal.configKey))
          session.createQuery(update).executeUpdate().await()
        }
          session.flush().await()
      }.toCompletionStage()
    }?.await()

    return true
  }
}
