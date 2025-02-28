/*
 * This file is generated by jOOQ.
 */
package dev.yidafu.blog.common.dao.tables.records

import dev.yidafu.blog.common.dao.tables.BArticle
import org.jooq.Record1
import org.jooq.impl.UpdatableRecordImpl
import java.time.LocalDateTime

/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
open class BArticleRecord() : UpdatableRecordImpl<BArticleRecord>(BArticle.B_ARTICLE) {
  open var id: Long?
    set(value): Unit = set(0, value)
    get(): Long? = get(0) as Long?

  open var createdAt: LocalDateTime?
    set(value): Unit = set(1, value)
    get(): LocalDateTime? = get(1) as LocalDateTime?

  open var updatedAt: LocalDateTime?
    set(value): Unit = set(2, value)
    get(): LocalDateTime? = get(2) as LocalDateTime?

  open var content: String?
    set(value): Unit = set(3, value)
    get(): String? = get(3) as String?

  open var cover: String?
    set(value): Unit = set(4, value)
    get(): String? = get(4) as String?

  open var hash: String?
    set(value): Unit = set(5, value)
    get(): String? = get(5) as String?

  open var html: String?
    set(value): Unit = set(6, value)
    get(): String? = get(6) as String?

  open var identifier: String?
    set(value): Unit = set(7, value)
    get(): String? = get(7) as String?

  open var series: String?
    set(value): Unit = set(8, value)
    get(): String? = get(8) as String?

  open var sourceType: String?
    set(value): Unit = set(9, value)
    get(): String? = get(9) as String?

  open var status: Int?
    set(value): Unit = set(10, value)
    get(): Int? = get(10) as Int?

  open var summary: String?
    set(value): Unit = set(11, value)
    get(): String? = get(11) as String?

  open var title: String?
    set(value): Unit = set(12, value)
    get(): String? = get(12) as String?

  // -------------------------------------------------------------------------
  // Primary key information
  // -------------------------------------------------------------------------

  override fun key(): Record1<Long?> = super.key() as Record1<Long?>

  /**
   * Create a detached, initialised BArticleRecord
   */
  constructor(
    id: Long? = null,
    createdAt: LocalDateTime? = null,
    updatedAt: LocalDateTime? = null,
    content: String? = null,
    cover: String? = null,
    hash: String? = null,
    html: String? = null,
    identifier: String? = null,
    series: String? = null,
    sourceType: String? = null,
    status: Int? = null,
    summary: String? = null,
    title: String? = null,
  ) : this() {
    this.id = id
    this.createdAt = createdAt
    this.updatedAt = updatedAt
    this.content = content
    this.cover = cover
    this.hash = hash
    this.html = html
    this.identifier = identifier
    this.series = series
    this.sourceType = sourceType
    this.status = status
    this.summary = summary
    this.title = title
    resetChangedOnNotNull()
  }
}
