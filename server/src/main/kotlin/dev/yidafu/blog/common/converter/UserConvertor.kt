package dev.yidafu.blog.common.converter

import dev.yidafu.blog.common.dao.tables.records.BUserRecord
import dev.yidafu.blog.common.modal.UserModal
import org.mapstruct.Mapper

@Mapper
interface UserConvertor {
  fun recordToModal(record: BUserRecord?): UserModal
}
