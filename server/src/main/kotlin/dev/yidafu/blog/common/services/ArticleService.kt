package dev.yidafu.blog.common.services

import dev.yidafu.blog.common.converter.ArticleConvertor
import dev.yidafu.blog.common.dao.tables.records.BArticleRecord
import dev.yidafu.blog.common.dao.tables.references.B_ARTICLE
import dev.yidafu.blog.common.modal.ArticleModel
import dev.yidafu.blog.common.query.PageQuery
import org.jooq.CloseableDSLContext
import org.koin.core.annotation.Single
import org.mapstruct.factory.Mappers

@Single
class ArticleService(
  private val context: CloseableDSLContext,
) : BaseService(context) {
  private val articleConvertor = Mappers.getMapper(ArticleConvertor::class.java)

  fun getListByPage(query: PageQuery): Pair<Int, List<ArticleModel>> {
    val count = context.fetchCount(B_ARTICLE)
    val articleRecords =
      context.selectFrom(B_ARTICLE)
        .limit(query.size)
        .offset(query.offset)
        .fetchArray()
    return count to articleConvertor.recordToModal(articleRecords.toList())
  }

  suspend fun getAll(): List<ArticleModel> =
    runDB {
      val articles: Array<BArticleRecord> =
        context.selectFrom(B_ARTICLE)
          .orderBy(B_ARTICLE.UPDATED_AT.desc())
          .fetchArray()
      articleConvertor.recordToModal(articles.toList())
    }

  suspend fun getOneByIdentifier(identifier: String): ArticleModel? =
    runDB {
      val article = context.selectFrom(B_ARTICLE).where(B_ARTICLE.IDENTIFIER.eq(identifier)).fetchOne()
      articleConvertor.recordToModal(article)
    }

  suspend fun getById(id: Int): ArticleModel? =
    runDB {
      val record = context.selectFrom(B_ARTICLE).where(B_ARTICLE.ID.eq(id.toLong())).fetchOne()
      articleConvertor.recordToModal(record)
    }

  suspend fun countAll() = runDB {
    context.selectCount().from(B_ARTICLE).fetchOne(0, Long::class.java) ?: 0
  }
}
