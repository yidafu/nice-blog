/*
 * This file is generated by jOOQ.
 */
package dev.yidafu.blog.common.dao.tables.records

import dev.yidafu.blog.common.dao.tables.BUserToken
import org.jooq.Record1
import org.jooq.impl.UpdatableRecordImpl
import java.time.LocalDateTime

/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
open class BUserTokenRecord() : UpdatableRecordImpl<BUserTokenRecord>(BUserToken.B_USER_TOKEN) {
  open var id: Long?
    set(value): Unit = set(0, value)
    get(): Long? = get(0) as Long?

  open var createdAt: LocalDateTime?
    set(value): Unit = set(1, value)
    get(): LocalDateTime? = get(1) as LocalDateTime?

  open var updatedAt: LocalDateTime?
    set(value): Unit = set(2, value)
    get(): LocalDateTime? = get(2) as LocalDateTime?

  open var createdBy: String?
    set(value): Unit = set(3, value)
    get(): String? = get(3) as String?

  open var description: String?
    set(value): Unit = set(4, value)
    get(): String? = get(4) as String?

  open var expiresAt: LocalDateTime?
    set(value): Unit = set(5, value)
    get(): LocalDateTime? = get(5) as LocalDateTime?

  open var name: String?
    set(value): Unit = set(6, value)
    get(): String? = get(6) as String?

  open var token: String?
    set(value): Unit = set(7, value)
    get(): String? = get(7) as String?

  // -------------------------------------------------------------------------
  // Primary key information
  // -------------------------------------------------------------------------

  override fun key(): Record1<Long?> = super.key() as Record1<Long?>

  /**
   * Create a detached, initialised BUserTokenRecord
   */
  constructor(
    id: Long? = null,
    createdAt: LocalDateTime? = null,
    updatedAt: LocalDateTime? = null,
    createdBy: String? = null,
    description: String? = null,
    expiresAt: LocalDateTime? = null,
    name: String? = null,
    token: String? = null,
  ) : this() {
    this.id = id
    this.createdAt = createdAt
    this.updatedAt = updatedAt
    this.createdBy = createdBy
    this.description = description
    this.expiresAt = expiresAt
    this.name = name
    this.token = token
    resetChangedOnNotNull()
  }
}
