package dev.yidafu.blog.common.vo

import dev.yidafu.blog.common.dto.SimpleSyncTaskDTO
import kotlin.math.ceil

open class PaginationVO<T>(
  val page: Int = 1,
  val size: Int = 10,
  val total: Int = 0,
  val list: List<T> = emptyList(),
) : AdminBaseVO() {
  val pageCount: Int = ceil(total.toDouble() / size).toInt()
}

class AdminSyncTaskListVO(
  page: Int = 1,
  size: Int = 10,
  total: Int = 0,
  list: List<SimpleSyncTaskDTO> = emptyList(),
) : PaginationVO<SimpleSyncTaskDTO>(
    page,
    size,
    total,
    list,
  )
