package dev.yidafu.blog.common.services

import dev.yidafu.blog.common.converter.UserConvertor
import dev.yidafu.blog.common.dao.tables.records.BUserRecord
import dev.yidafu.blog.common.dao.tables.references.B_USER
import dev.yidafu.blog.common.modal.UserModal
import org.jooq.CloseableDSLContext
import org.koin.core.annotation.Single
import org.mapstruct.factory.Mappers

@Single
class UserService(
  private val context: CloseableDSLContext,
) : BaseService(context) {
  private val userConvertor = Mappers.getMapper(UserConvertor::class.java)

  internal suspend fun getUserByUsername(username: String): UserModal? =
    runDB {
      val userRecord: BUserRecord? = context.selectFrom(B_USER).where(B_USER.USERNAME.eq(username)).fetchOne()

      userConvertor.recordToModal(userRecord)
    }
}
