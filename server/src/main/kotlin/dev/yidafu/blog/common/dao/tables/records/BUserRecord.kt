/*
 * This file is generated by jOOQ.
 */
package dev.yidafu.blog.common.dao.tables.records


import dev.yidafu.blog.common.dao.tables.BUser

import java.time.LocalDateTime

import org.jooq.Record1
import org.jooq.impl.UpdatableRecordImpl


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
open class BUserRecord() : UpdatableRecordImpl<BUserRecord>(BUser.B_USER) {

    open var id: Long?
        set(value): Unit = set(0, value)
        get(): Long? = get(0) as Long?

    open var createdAt: LocalDateTime?
        set(value): Unit = set(1, value)
        get(): LocalDateTime? = get(1) as LocalDateTime?

    open var updatedAt: LocalDateTime?
        set(value): Unit = set(2, value)
        get(): LocalDateTime? = get(2) as LocalDateTime?

    open var email: String?
        set(value): Unit = set(3, value)
        get(): String? = get(3) as String?

    open var loginCount: String?
        set(value): Unit = set(4, value)
        get(): String? = get(4) as String?

    open var password: String?
        set(value): Unit = set(5, value)
        get(): String? = get(5) as String?

    open var status: Int?
        set(value): Unit = set(6, value)
        get(): Int? = get(6) as Int?

    open var username: String?
        set(value): Unit = set(7, value)
        get(): String? = get(7) as String?

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    override fun key(): Record1<Long?> = super.key() as Record1<Long?>

    /**
     * Create a detached, initialised BUserRecord
     */
    constructor(id: Long? = null, createdAt: LocalDateTime? = null, updatedAt: LocalDateTime? = null, email: String? = null, loginCount: String? = null, password: String? = null, status: Int? = null, username: String? = null): this() {
        this.id = id
        this.createdAt = createdAt
        this.updatedAt = updatedAt
        this.email = email
        this.loginCount = loginCount
        this.password = password
        this.status = status
        this.username = username
        resetChangedOnNotNull()
    }
}
