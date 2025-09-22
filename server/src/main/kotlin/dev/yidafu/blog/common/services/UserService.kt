package dev.yidafu.blog.common.services

import dev.yidafu.blog.common.db.dao.UserEntity
import dev.yidafu.blog.common.db.tables.UserTable
import org.jetbrains.exposed.v1.core.eq
import org.koin.core.annotation.Single

@Single
class UserService : ExposedBaseService() {
  /**
   * 根据用户名获取用户
   */
  internal suspend fun getUserByUsername(username: String): UserEntity? =
    runDB {
      UserEntity.find { UserTable.username eq username }
        .singleOrNull()
    }
}
