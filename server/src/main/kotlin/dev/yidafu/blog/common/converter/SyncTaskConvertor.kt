package dev.yidafu.blog.common.converter

import dev.yidafu.blog.common.dao.tables.records.BSyncTaskRecord
import dev.yidafu.blog.common.dto.SimpleSyncTaskDTO
import dev.yidafu.blog.common.dto.SyncTaskDTO
import dev.yidafu.blog.common.modal.SyncTaskModel
import org.mapstruct.Mapper

@Mapper
interface SyncTaskConvertor {
  fun recordToModal(record: BSyncTaskRecord?): SyncTaskModel

  fun recordToModal(recordList: List<BSyncTaskRecord>): List<SyncTaskModel>

  fun toVO(modal: SyncTaskModel): SyncTaskDTO

  fun toVOList(modal: List<SyncTaskModel>): List<SimpleSyncTaskDTO>
}
