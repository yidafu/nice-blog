package dev.yidafu.blog.fe.service

import dev.yidafu.blog.common.converter.AccessLogConvertor
import dev.yidafu.blog.common.dao.tables.references.B_ACCESS_LOG
import dev.yidafu.blog.common.dto.AccessLogDTO
import org.jooq.CloseableDSLContext
import org.koin.core.annotation.Single
import org.mapstruct.factory.Mappers
import org.slf4j.LoggerFactory

@Single
class AccessLogService(
  private val context: CloseableDSLContext,
) {
  private val log = LoggerFactory.getLogger(AccessLogService::class.java)
  private val convertor = Mappers.getMapper(AccessLogConvertor::class.java)

  fun saveLog(dto: AccessLogDTO): Boolean {
    try {
      val newLog = context.newRecord(B_ACCESS_LOG)

      convertor.mapToRecord(dto, newLog)
      return newLog.store() > 0
    } catch (e: Exception) {
      log.warn("保存访问日志失败 {}", dto, e)
    }
    return false
  }
}
