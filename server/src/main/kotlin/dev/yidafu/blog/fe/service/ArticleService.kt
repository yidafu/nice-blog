package dev.yidafu.blog.fe.service

import dev.yidafu.blog.common.converter.ArticleConvertor
import dev.yidafu.blog.common.dao.tables.records.BArticleRecord
import dev.yidafu.blog.common.dao.tables.references.B_ARTICLE
import dev.yidafu.blog.common.modal.ArticleModel
import dev.yidafu.blog.common.services.BaseService
import org.jooq.CloseableDSLContext
import org.koin.core.annotation.Single
import org.mapstruct.factory.Mappers
import org.slf4j.LoggerFactory

@Single
class ArticleService(
  private val context:CloseableDSLContext,
) : BaseService(context) {
  private val log = LoggerFactory.getLogger(ArticleService::class.java)
  private val articleConvertor = Mappers.getMapper(ArticleConvertor::class.java)

  suspend fun getAll(): List<ArticleModel> = runDB{
    val articles: Array<BArticleRecord> = context.selectFrom(B_ARTICLE).fetchArray()
    articleConvertor.recordToModal(articles.toList())
  }

  suspend fun getOneByIdentifier(identifier: String): ArticleModel? = runDB {
    val article = context.selectFrom(B_ARTICLE).where(B_ARTICLE.IDENTIFIER.eq(identifier)).fetchOne()
    articleConvertor.recordToModal(article)
  }
}
