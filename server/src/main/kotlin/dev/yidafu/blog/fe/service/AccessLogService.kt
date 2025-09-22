package dev.yidafu.blog.fe.service

import dev.yidafu.blog.common.db.dao.AccessLogEntity
import dev.yidafu.blog.common.dto.AccessLogDTO
import dev.yidafu.blog.common.ext.now
import dev.yidafu.blog.common.services.BaseService
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toKotlinLocalDateTime
import org.koin.core.annotation.Single
import org.slf4j.LoggerFactory

@Single
class AccessLogService : BaseService() {
  private val log = LoggerFactory.getLogger(AccessLogService::class.java)

  fun saveLog(dto: AccessLogDTO): Boolean {
    return try {
      runDBBlocking {
        AccessLogEntity.new {
          ip = dto.ip
          ua = dto.ua
          referrerUrl = dto.referrerUrl
          sourceUrl = dto.sourceUrl
          accessTime =LocalDateTime.now()
        }
        true
      }
    } catch (e: Exception) {
      log.warn("保存访问日志失败 {}", dto, e)
      false
    }
  }

  suspend fun countAll(): Long =
    runDB {
      AccessLogEntity.all().count()
    }
}
