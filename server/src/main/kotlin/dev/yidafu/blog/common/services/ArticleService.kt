package dev.yidafu.blog.common.services

import dev.yidafu.blog.common.converter.ArticleConvertor
import dev.yidafu.blog.common.db.dao.ArticleEntity
import dev.yidafu.blog.common.db.tables.ArticleTable
import dev.yidafu.blog.common.query.PageQuery
import dev.yidafu.blog.common.annotation.Service
import org.jetbrains.exposed.v1.core.SortOrder
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.mapstruct.factory.Mappers

interface ArticleService {
  fun getListByPage(query: PageQuery): Pair<Int, List<ArticleEntity>>

  suspend fun getAll(): List<ArticleEntity>

  suspend fun getOneByIdentifier(identifier: String): ArticleEntity?

  suspend fun getById(id: Int): ArticleEntity?

  suspend fun countAll(): Long
}

@Service
class ArticleServiceImpl : ArticleService, ExposedBaseService() {
  val convertor = Mappers.getMapper(ArticleConvertor::class.java)

  /**
   * 分页获取文章列表
   */
  override fun getListByPage(query: PageQuery): Pair<Int, List<ArticleEntity>> {
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
  override suspend fun getAll(): List<ArticleEntity> =
    runDB {
      ArticleEntity.all()
        .orderBy(ArticleTable.updatedAt to SortOrder.DESC).toList()
    }

  /**
   * 根据标识符获取单个文章
   */
  override suspend fun getOneByIdentifier(identifier: String): ArticleEntity? =
    runDB {
      ArticleEntity.find { ArticleTable.identifier eq identifier }
        .singleOrNull()
    }

  /**
   * 根据ID获取文章
   */
  override suspend fun getById(id: Int): ArticleEntity? =
    runDB {
      ArticleEntity.findById(id)
    }

  /**
   * 获取文章总数
   */
  override suspend fun countAll(): Long =
    runDB {
      ArticleEntity.all().count()
    }
}
