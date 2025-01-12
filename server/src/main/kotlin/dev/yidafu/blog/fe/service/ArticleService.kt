package dev.yidafu.blog.fe.service

import dev.yidafu.blog.common.converter.ArticleConvertor
import dev.yidafu.blog.common.converter.UserConvertor
import dev.yidafu.blog.common.dao.tables.records.BArticleRecord
import dev.yidafu.blog.common.dao.tables.references.B_ARTICLE
import dev.yidafu.blog.common.dao.tables.references.B_USER
import dev.yidafu.blog.common.modal.ArticleStatus
import dev.yidafu.blog.common.modal.ArticleModel
import jakarta.persistence.EntityManagerFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.future.await
import kotlinx.coroutines.withContext
import org.hibernate.reactive.stage.Stage.SessionFactory
import org.jooq.DSLContext
import org.koin.core.annotation.Single
import org.mapstruct.factory.Mappers
import org.slf4j.LoggerFactory

@Single
class ArticleService(
  private val emf: EntityManagerFactory,
  private val sessionFactory: SessionFactory,
  private val context: DSLContext,
) {
  private val log = LoggerFactory.getLogger(ArticleService::class.java)
  private val articleConvertor = Mappers.getMapper(ArticleConvertor::class.java)

  suspend fun getAll(): List<ArticleModel> = withContext(Dispatchers.IO){
    val articles: Array<BArticleRecord> = context.selectFrom(B_ARTICLE).fetchArray()
    articleConvertor.recordToModal(articles.toList())
  }

  suspend fun getOneByIdentifier(identifier: String): ArticleModel? = withContext(Dispatchers.IO) {
    val article = context.selectFrom(B_ARTICLE).where(B_ARTICLE.IDENTIFIER.eq(identifier)).fetchOne()
    articleConvertor.recordToModal(article)
  }
}
