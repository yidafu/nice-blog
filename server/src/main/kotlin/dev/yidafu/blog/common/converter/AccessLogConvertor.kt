package dev.yidafu.blog.common.converter

import dev.yidafu.blog.common.dao.tables.records.BAccessLogRecord
import dev.yidafu.blog.common.dto.AccessLogDTO
import org.mapstruct.Mapper
import org.mapstruct.MappingTarget

@Mapper
interface AccessLogConvertor {
  fun mapToRecord(
    dto: AccessLogDTO,
    @MappingTarget record: BAccessLogRecord,
  )
}
