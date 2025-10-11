package dev.yidafu.blog.common.db.dao

import dev.yidafu.blog.common.db.tables.ArticleTable
import dev.yidafu.blog.common.db.tables.SyncTaskTable
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass

class ArticleEntity(id: EntityID<Int>) : IntEntity(id) {
  companion object : IntEntityClass<ArticleEntity>(ArticleTable)

  private var entityId by SyncTaskTable.id
  var tId: Int = entityId.value
  var title by ArticleTable.title
  var cover by ArticleTable.cover
  var identifier by ArticleTable.identifier
  var series by ArticleTable.series
  var status by ArticleTable.status
  var summary by ArticleTable.summary
  var content by ArticleTable.content
  var html by ArticleTable.html
  var sourceType by ArticleTable.sourceType
  var hash by ArticleTable.hash
  var createdAt by ArticleTable.createdAt
  var updatedAt by ArticleTable.updatedAt

  override fun toString(): String {
    return "Article(id=$id, title=$title, sourceType=$sourceType, status=$status)"
  }
}
