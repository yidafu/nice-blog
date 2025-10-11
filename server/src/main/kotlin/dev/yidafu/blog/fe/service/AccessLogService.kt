package dev.yidafu.blog.fe.service

import dev.yidafu.blog.common.db.dao.AccessLogEntity
import dev.yidafu.blog.common.dto.AccessLogDTO
import dev.yidafu.blog.common.ext.now
import dev.yidafu.blog.common.services.BaseService
import dev.yidafu.blog.common.annotation.Service
import kotlinx.datetime.LocalDateTime
import org.slf4j.LoggerFactory

interface AccessLogService {
  fun saveLog(dto: AccessLogDTO): Boolean

  suspend fun countAll(): Long
}

@Service
class AccessLogServiceImpl : AccessLogService, BaseService() {
  private val log = LoggerFactory.getLogger(AccessLogService::class.java)

  override fun saveLog(dto: AccessLogDTO): Boolean {
    return try {
        AccessLogEntity.new {
          ip = dto.ip
          ua = dto.ua
          referrerUrl = dto.referrerUrl
          sourceUrl = dto.sourceUrl
          accessTime = LocalDateTime.now()
        }
        true
    } catch (e: Exception) {
      log.warn("保存访问日志失败 {}", dto, e)
      false
    }
  }

  override suspend fun countAll(): Long =
    runDB {
      AccessLogEntity.all().count()
    }
}
