package dev.yidafu.blog.service

import dev.yidafu.blog.modal.ArticleStatus
import dev.yidafu.blog.modal.Articlemodel
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

  suspend fun getAll(): List<Articlemodel>? {
    val criteriaBuilder = sessionFactory.criteriaBuilder

   return sessionFactory.withSession { session ->
      val query = criteriaBuilder.createQuery(Articlemodel::class.java)
        val form = query.from(Articlemodel::class.java)
        query.select(form)
          .where(
            criteriaBuilder.equal(form.get<String>(Articlemodel::status.name), ArticleStatus.Candidate.ordinal)
          )
        log.info("开始查询文章列表")
        session.createQuery(query).resultList
    }.await()
  }
}
