package dev.yidafu.blog.common.services

import dev.yidafu.blog.common.converter.ArticleConvertor
import dev.yidafu.blog.common.dao.tables.records.BArticleRecord
import dev.yidafu.blog.common.dao.tables.references.B_ARTICLE
import dev.yidafu.blog.common.modal.ArticleModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.future.await
import kotlinx.coroutines.withContext
import org.hibernate.reactive.stage.Stage.SessionFactory
import org.jooq.DSLContext
import org.koin.core.annotation.Single
import org.mapstruct.factory.Mappers

@Single
class ArticleService(
  private val context: DSLContext,
) {
  private val articleConvertor = Mappers.getMapper(ArticleConvertor::class.java)

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
    val newArticleRecord: BArticleRecord = context.newRecord(B_ARTICLE)
    articleConvertor.mapToRecord(article, newArticleRecord)
    newArticleRecord.store()
  }

  private suspend fun updateArticle(article: ArticleModel) {
    val oldRecord: BArticleRecord? = context.selectFrom(B_ARTICLE).where(B_ARTICLE.ID.eq(article.id)).fetchOne()
    articleConvertor.mapToRecord(article, oldRecord)
    oldRecord?.store()
  }
  suspend fun findArticleByName(name: String): ArticleModel?  = withContext(Dispatchers.IO) {
    val article: BArticleRecord? = context.selectFrom(B_ARTICLE).where(
      B_ARTICLE.IDENTIFIER.eq(name)
    ).fetchOne()
     articleConvertor.recordToModal(article)
  }
}
