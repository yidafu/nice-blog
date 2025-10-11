package dev.yidafu.blog.common.db.dao

import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.IntEntity

open class BaseEntity(id: EntityID<Int>) : IntEntity(id)
