package dev.yidafu.blog.admin.services

import dev.yidafu.blog.common.Routes
import dev.yidafu.blog.common.modal.SyncTaskModel
import dev.yidafu.blog.common.modal.SyncTaskStatus
import kotlinx.coroutines.future.await
import org.hibernate.reactive.stage.Stage.SessionFactory
import org.koin.core.annotation.Single

@Single
class SyncTaskService(
  private val sessionFactory: SessionFactory,
) {
  suspend fun createSyncTask(uuid: String): Boolean {
    val task = SyncTaskModel(
      uuid = uuid,
      status = SyncTaskStatus.Created,
      callbackUrl =  Routes.SYNC_LOG_URL.replace(":uuid", uuid)
    )
    sessionFactory.withSession { session ->
      session.persist(task)
      session.flush()
    }.await()

    return true
  }

  suspend fun appendLog(uuid: String, log: String): Boolean {
    sessionFactory.withSession { session ->
       val query = session.createNamedQuery<String>(SyncTaskModel.APPEND_LOG_TEXT)
      query.setParameter("logs", log)
      query.setParameter("uuid", uuid)
      query.executeUpdate()
    }.await()
    return true
  }

  suspend fun changeStatus(uuid: String, status: SyncTaskStatus): Boolean {
    val builder = sessionFactory.criteriaBuilder
    sessionFactory.withSession { session ->
      val update = builder.createCriteriaUpdate(SyncTaskModel::class.java)
      val from = update.from(SyncTaskModel::class.java)
      update.set(SyncTaskModel::status.name, status)

      update.where(builder.equal(from.get<String>(SyncTaskModel::uuid.name), uuid))

      session.createQuery(update).executeUpdate()
    }.await()
    return true
  }

  suspend fun getSyncLog(uuid: String): SyncTaskModel? {
    val builder = sessionFactory.criteriaBuilder
    return sessionFactory.withSession { session ->
      val query = builder.createQuery(SyncTaskModel::class.java)
      val from = query.from(SyncTaskModel::class.java)
      query.select(from)
      query.where(builder.equal(from.get<String>(SyncTaskModel::uuid.name), uuid))

      val result = session.createQuery(query).singleResult
      result
    }.await()
  }
}
