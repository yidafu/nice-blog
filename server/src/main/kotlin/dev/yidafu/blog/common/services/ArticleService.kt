package dev.yidafu.blog.common.services

import dev.yidafu.blog.common.converter.ArticleConvertor
import dev.yidafu.blog.common.db.dao.ArticleEntity
import dev.yidafu.blog.common.db.tables.ArticleTable
import dev.yidafu.blog.common.query.PageQuery
import org.jetbrains.exposed.v1.core.SortOrder
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.koin.core.annotation.Single
import org.mapstruct.factory.Mappers

@Single
class ArticleService : ExposedBaseService() {
  val convertor = Mappers.getMapper(ArticleConvertor::class.java)

  /**
   * 分页获取文章列表
   */
  fun getListByPage(query: PageQuery): Pair<Int, List<ArticleEntity>> {
    return transaction {
      val count = ArticleEntity.all().count().toInt()
      val articles =
        ArticleEntity.all()
          .offset(query.offset.toLong())
          .limit(query.size).toList()
      count to articles
    }
  }

  /**
   * 获取所有文章
   */
  suspend fun getAll(): List<ArticleEntity> =
    runDB {
      ArticleEntity.all()
        .orderBy(ArticleTable.updatedAt to SortOrder.DESC).toList()
    }

  /**
   * 根据标识符获取单个文章
   */
  suspend fun getOneByIdentifier(identifier: String): ArticleEntity? =
    runDB {
      ArticleEntity.find { ArticleTable.identifier eq identifier }
        .singleOrNull()
    }

  /**
   * 根据ID获取文章
   */
  suspend fun getById(id: Int): ArticleEntity? =
    runDB {
      ArticleEntity.findById(id)
    }

  /**
   * 获取文章总数
   */
  suspend fun countAll() =
    runDB {
      ArticleEntity.all().count()
    }
}
