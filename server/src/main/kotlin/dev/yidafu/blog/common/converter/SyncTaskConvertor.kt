package dev.yidafu.blog.common.converter

import dev.yidafu.blog.common.dao.tables.records.BSyncTaskRecord
import dev.yidafu.blog.common.modal.SyncTaskModel
import org.mapstruct.Mapper

@Mapper
interface SyncTaskConvertor {
  fun recordToModal(record: BSyncTaskRecord?): SyncTaskModel
}
