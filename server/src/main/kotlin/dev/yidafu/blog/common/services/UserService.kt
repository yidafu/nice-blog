package dev.yidafu.blog.common.services

import dev.yidafu.blog.common.db.dao.UserEntity
import dev.yidafu.blog.common.db.tables.UserTable
import dev.yidafu.blog.common.annotation.Service
import org.jetbrains.exposed.v1.core.eq

interface UserService {
  suspend fun getUserByUsername(username: String): UserEntity?
}

@Service
class UserServiceImpl : ExposedBaseService() {
  /**
   * 根据用户名获取用户
   */
  internal suspend fun getUserByUsername(username: String): UserEntity? =
    runDB {
      UserEntity.find { UserTable.username eq username }
        .singleOrNull()
    }
}
