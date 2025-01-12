package dev.yidafu.blog.common.modal

import dev.yidafu.blog.common.modal.SyncTaskModel.Companion.APPEND_LOG_TEXT
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalTime
import kotlin.uuid.Uuid

enum class SyncTaskStatus() {
  Created,
  Running,
  Finished,
  Failed
}

@NamedQueries(
  NamedQuery(
    name = APPEND_LOG_TEXT,
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
) : BaseModel()
{
  companion object {
    const val APPEND_LOG_TEXT = "APPEND_LOG_TEXT"
  }
}
