package dev.yidafu.blog.common.vo

import dev.yidafu.blog.common.dto.SimpleSyncTaskDTO
import kotlinx.serialization.Serializable
import kotlin.math.ceil


@Serializable
open class PaginationVO<T>(
  open val page: Int = 1,
  val size: Int = 10,
  val total: Int = 0,
  val list: List<T> = emptyList(),
)  {
  val pageCount: Int = ceil(total.toDouble() / size).toInt()
}
