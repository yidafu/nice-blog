package dev.yidafu.blog.common.services

import dev.yidafu.blog.common.modal.ArticleModel
import kotlinx.coroutines.future.await
import org.hibernate.reactive.stage.Stage.SessionFactory
import org.koin.core.annotation.Single

@Single
class ArticleService(
  private val sessionFactory: SessionFactory,
) {
  suspend fun saveArticle(article: ArticleModel): Boolean {
    if (article.identifier == null) {
      return false
    }
    val oldArticle = findArticleByName(article.identifier!!)
    if (oldArticle == null) {
      // insert new article
        createArticle(article)
    } else {
      // update article
      article.id = oldArticle.id
      updateArticle(article)
    }

    return true
  }
  private suspend fun createArticle(article: ArticleModel) {
    sessionFactory.withSession { session ->
      session.persist(article)
      session.flush()
    }.await()
  }

  private suspend fun updateArticle(article: ArticleModel) {
    sessionFactory.withSession { session ->
      session.merge(article)
      session.flush()
    }.await()
  }
  suspend fun findArticleByName(name: String): ArticleModel? {
    val criteriaBuilder = sessionFactory.criteriaBuilder

    return sessionFactory.withSession { session ->
      val query = criteriaBuilder.createQuery(ArticleModel::class.java)
      val from = query.from(ArticleModel::class.java)
      query.where(criteriaBuilder.equal(from.get<String>(ArticleModel::identifier.name), name))
      query.select(from)
      session.createQuery(query).singleResultOrNull
    }.await()
  }
}
