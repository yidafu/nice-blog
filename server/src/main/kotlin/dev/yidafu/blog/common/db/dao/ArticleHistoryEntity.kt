package dev.yidafu.blog.common.db.dao

import dev.yidafu.blog.common.db.tables.ArticleHistoryTable
import dev.yidafu.blog.common.db.tables.SyncTaskTable
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass

class ArticleHistoryEntity(id: EntityID<Int>) : IntEntity(id) {
  companion object : IntEntityClass<ArticleHistoryEntity>(ArticleHistoryTable)


  private var entityId by SyncTaskTable.id
  var tId: Int = entityId.value
  var articleId by ArticleHistoryTable.articleId
  var rawContent by ArticleHistoryTable.rawContent
  var renderedContent by ArticleHistoryTable.renderedContent
  var createdAt by ArticleHistoryTable.createdAt
  var updatedAt by ArticleHistoryTable.updatedAt

  override fun toString(): String {
    return "ArticleHistory(id=$id, articleId=$articleId)"
  }
}
