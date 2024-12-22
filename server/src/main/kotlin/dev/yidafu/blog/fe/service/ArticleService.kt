package dev.yidafu.blog.fe.service

import dev.yidafu.blog.common.modal.ArticleStatus
import dev.yidafu.blog.common.modal.ArticleModel
import jakarta.persistence.EntityManagerFactory
import kotlinx.coroutines.future.await
import org.hibernate.reactive.stage.Stage.SessionFactory
import org.koin.core.annotation.Single
import org.slf4j.LoggerFactory

@Single
class ArticleService(
  private val emf: EntityManagerFactory,
  private val sessionFactory: SessionFactory,
) {
  private val log = LoggerFactory.getLogger(ArticleService::class.java)

  suspend fun getAll(): List<ArticleModel> {
    val criteriaBuilder = sessionFactory.criteriaBuilder

    return sessionFactory.withSession { session ->
      val query = criteriaBuilder.createQuery(ArticleModel::class.java)
      val form = query.from(ArticleModel::class.java)
      query.select(form)
        .where(
          criteriaBuilder.ge(form.get(ArticleModel::status.name), ArticleStatus.Candidate.ordinal)
        )
      log.info("开始查询文章列表")
      session.createQuery(query).resultList
    }.await() ?: emptyList()
  }

  suspend fun getOneByIdentifier(identifier: String): ArticleModel? {
    val criteriaBuilder = sessionFactory.criteriaBuilder

    return sessionFactory.withSession { session ->
      val query = criteriaBuilder.createQuery(ArticleModel::class.java)
      val form = query.from(ArticleModel::class.java)
      query.select(form)
        .where(
          criteriaBuilder.equal(form.get<String>(ArticleModel::identifier.name), identifier)
        )
      session.createQuery(query).singleResultOrNull
    }.await()
  }
}
