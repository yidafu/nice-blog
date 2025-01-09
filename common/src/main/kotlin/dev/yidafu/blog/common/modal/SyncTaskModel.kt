package dev.yidafu.blog.common.modal

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
    name = "APPEND_LOG_TEXT",
    query = "UPDATE b_sync_task  set logs=CONCAT(logs, :logs) where uuid = :uuid"
  )
)
@Entity(name = "b_sync_task")
class SyncTaskModel(
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  var id: Long? = null,

  @CreationTimestamp
  @Column(name = "created_at")
  var createdAt: LocalTime? = null,

  @UpdateTimestamp
  @Column(name = "updated_at")
  var updatedAt: LocalTime? = null,
  @Column
  var callbackUrl: String? = null,

  @Column
  var uuid: String? = null,

  @Enumerated
  @Column
  var status: SyncTaskStatus? = null,

  @Column(columnDefinition = "MEDIUMTEXT")
  var logs: String? = null,
) /*: BaseModel() */
{
  companion object {
    const val APPEND_LOG_TEXT = "APPEND_LOG_TEXT"
  }
}
