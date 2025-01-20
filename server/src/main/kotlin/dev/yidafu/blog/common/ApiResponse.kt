package dev.yidafu.blog.common

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse<T>(
  val code: Int,
  val data: T? = null,
  val message: String,
) {
  companion object {
    fun <T> success(data: T): ApiResponse<T> {
      return ApiResponse<T>(0, data, "ok")
    }

    fun <T> fail(
      code: Int,
      message: String,
    ): ApiResponse<T> {
      return ApiResponse(code, null, message)
    }
  }
}
