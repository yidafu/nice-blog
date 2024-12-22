package dev.yidafu.blog.common.services

import dev.yidafu.blog.common.modal.UserModal
import kotlinx.coroutines.future.await
import org.hibernate.reactive.stage.Stage.SessionFactory
import org.koin.core.annotation.Single

@Single
class UserService(
  private val sessionFactory: SessionFactory,
) {
  internal suspend fun getUserByUsername(username: String): UserModal? {
    val criteriaBuilder = sessionFactory.criteriaBuilder

    return sessionFactory.withSession { session ->
      val query = criteriaBuilder.createQuery(UserModal::class.java)
      val from = query.from(UserModal::class.java)
      query.where(criteriaBuilder.equal(from.get<String>(UserModal::username.name), username))
      query.select(from)
      session.createQuery(query).singleResultOrNull
    }.await()
  }
}
