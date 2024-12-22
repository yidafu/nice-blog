package dev.yidafu.blog.common.modal

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Enumerated
import jakarta.persistence.NamedQueries
import jakarta.persistence.NamedQuery
import kotlin.uuid.Uuid

enum class SyncTaskStatus() {
  Created,
  Running,
  Finished,
  Failed
}

@NamedQueries(
  NamedQuery(
    name = "APPEND_LOG_TEXT",
    query = "UPDATE b_sync_task  set logs=CONCAT(logs, :logs) where uuid = :uuid"
  )
)
@Entity(name = "b_sync_task")
class SyncTaskModel(
  @Column
  var callbackUrl: String? = null,

  @Column
  var uuid: String? = null,

  @Enumerated
  @Column
  var status: SyncTaskStatus? = null,

  @Column(columnDefinition = "MEDIUMTEXT")
  var logs: String? = null,
) : BaseModel() {
  companion object {
    const val APPEND_LOG_TEXT = "APPEND_LOG_TEXT"
  }
}
