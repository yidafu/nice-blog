package dev.yidafu.blog.common.converter

import dev.yidafu.blog.common.dao.tables.records.BSyncTaskRecord
import dev.yidafu.blog.common.modal.SyncTaskModel
import dev.yidafu.blog.common.vo.SyncTaskVO
import org.mapstruct.Mapper

@Mapper
interface SyncTaskConvertor {
  fun recordToModal(record: BSyncTaskRecord?): SyncTaskModel

  fun recordToModal(recordList: List<BSyncTaskRecord>): List<SyncTaskModel>

  fun toVO(modal: SyncTaskModel): SyncTaskVO

  fun toVOList(modal: List<SyncTaskModel>): List<SyncTaskVO>
}
