package dev.yidafu.blog.common.modal

import jakarta.persistence.Column
import jakarta.persistence.Entity

@Entity(name = "b_sync_task")
class SyncTaskModel(
  @Column
  var callbackUrl: String? = null,
  @Column
  var status: Byte? = null,
  @Column
  var logs: String? = null
) : BaseModel()
