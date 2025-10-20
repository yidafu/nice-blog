package dev.yidafu.nicemaker.platform.io

import kotlinx.datetime.LocalDateTime
import kotlinx.io.files.Path

/**
 * 跨平台文件时间工具
 * 用于获取文件的创建时间和修改时间
 */
expect object FileTimeUtils {
  /**
   * 获取文件创建时间
   * @param path 文件路径
   * @return 创建时间，如果无法获取则返回null
   */
  fun getCreationTime(path: Path): LocalDateTime?

  /**
   * 获取文件最后修改时间
   * @param path 文件路径
   * @return 修改时间，如果无法获取则返回null
   */
  fun getModifiedTime(path: Path): LocalDateTime?
}

