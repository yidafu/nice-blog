/*
 * This file is generated by jOOQ.
 */
package dev.yidafu.blog.common.dao.tables

import dev.yidafu.blog.common.dao.DefaultSchema
import dev.yidafu.blog.common.dao.keys.CONSTRAINT_8
import dev.yidafu.blog.common.dao.tables.records.BArticleHistoryRecord
import org.jooq.Condition
import org.jooq.Field
import org.jooq.ForeignKey
import org.jooq.Identity
import org.jooq.InverseForeignKey
import org.jooq.Name
import org.jooq.PlainSQL
import org.jooq.QueryPart
import org.jooq.Record
import org.jooq.SQL
import org.jooq.Schema
import org.jooq.Select
import org.jooq.Stringly
import org.jooq.Table
import org.jooq.TableField
import org.jooq.TableOptions
import org.jooq.UniqueKey
import org.jooq.impl.DSL
import org.jooq.impl.SQLDataType
import org.jooq.impl.TableImpl
import java.time.LocalDateTime
import kotlin.collections.Collection

/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
open class BArticleHistory(
  alias: Name,
  path: Table<out Record>?,
  childPath: ForeignKey<out Record, BArticleHistoryRecord>?,
  parentPath: InverseForeignKey<out Record, BArticleHistoryRecord>?,
  aliased: Table<BArticleHistoryRecord>?,
  parameters: Array<Field<*>?>?,
  where: Condition?,
) : TableImpl<BArticleHistoryRecord>(
    alias,
    DefaultSchema.DEFAULT_SCHEMA,
    path,
    childPath,
    parentPath,
    aliased,
    parameters,
    DSL.comment(""),
    TableOptions.table(),
    where,
  ) {
  companion object {
    /**
     * The reference instance of <code>B_ARTICLE_HISTORY</code>
     */
    val B_ARTICLE_HISTORY: BArticleHistory = BArticleHistory()
  }

  /**
   * The class holding records for this type
   */
  override fun getRecordType(): Class<BArticleHistoryRecord> = BArticleHistoryRecord::class.java

  /**
   * The column <code>B_ARTICLE_HISTORY.ID</code>.
   */
  val ID: TableField<BArticleHistoryRecord, Long?> =
    createField(DSL.name("ID"), SQLDataType.BIGINT.nullable(false).identity(true), this, "")

  /**
   * The column <code>B_ARTICLE_HISTORY.CREATED_AT</code>.
   */
  val CREATED_AT: TableField<BArticleHistoryRecord, LocalDateTime?> =
    createField(
      DSL.name("CREATED_AT"),
      SQLDataType.LOCALDATETIME(6).defaultValue(DSL.field(DSL.raw("CURRENT_TIMESTAMP"), SQLDataType.LOCALDATETIME)),
      this,
      "",
    )

  /**
   * The column <code>B_ARTICLE_HISTORY.UPDATED_AT</code>.
   */
  val UPDATED_AT: TableField<BArticleHistoryRecord, LocalDateTime?> =
    createField(
      DSL.name("UPDATED_AT"),
      SQLDataType.LOCALDATETIME(6).defaultValue(DSL.field(DSL.raw("CURRENT_TIMESTAMP"), SQLDataType.LOCALDATETIME)),
      this,
      "",
    )

  /**
   * The column <code>B_ARTICLE_HISTORY.ARTICLE_ID</code>.
   */
  val ARTICLE_ID: TableField<BArticleHistoryRecord, Long?> = createField(DSL.name("ARTICLE_ID"), SQLDataType.BIGINT, this, "")

  /**
   * The column <code>B_ARTICLE_HISTORY.RAW_CONTENT</code>.
   */
  val RAW_CONTENT: TableField<BArticleHistoryRecord, String?> = createField(DSL.name("RAW_CONTENT"), SQLDataType.VARCHAR(255), this, "")

  /**
   * The column <code>B_ARTICLE_HISTORY.RENDERED_CONTENT</code>.
   */
  val RENDERED_CONTENT: TableField<BArticleHistoryRecord, String?> =
    createField(DSL.name("RENDERED_CONTENT"), SQLDataType.VARCHAR(255), this, "")

  private constructor(alias: Name, aliased: Table<BArticleHistoryRecord>?) : this(alias, null, null, null, aliased, null, null)
  private constructor(
    alias: Name,
    aliased: Table<BArticleHistoryRecord>?,
    parameters: Array<Field<*>?>?,
  ) : this(alias, null, null, null, aliased, parameters, null)
  private constructor(
    alias: Name,
    aliased: Table<BArticleHistoryRecord>?,
    where: Condition?,
  ) : this(alias, null, null, null, aliased, null, where)

  /**
   * Create an aliased <code>B_ARTICLE_HISTORY</code> table reference
   */
  constructor(alias: String) : this(DSL.name(alias))

  /**
   * Create an aliased <code>B_ARTICLE_HISTORY</code> table reference
   */
  constructor(alias: Name) : this(alias, null)

  /**
   * Create a <code>B_ARTICLE_HISTORY</code> table reference
   */
  constructor() : this(DSL.name("B_ARTICLE_HISTORY"), null)

  override fun getSchema(): Schema? = if (aliased()) null else DefaultSchema.DEFAULT_SCHEMA

  override fun getIdentity(): Identity<BArticleHistoryRecord, Long?> = super.getIdentity() as Identity<BArticleHistoryRecord, Long?>

  override fun getPrimaryKey(): UniqueKey<BArticleHistoryRecord> = CONSTRAINT_8

  override fun `as`(alias: String): BArticleHistory = BArticleHistory(DSL.name(alias), this)

  override fun `as`(alias: Name): BArticleHistory = BArticleHistory(alias, this)

  override fun `as`(alias: Table<*>): BArticleHistory = BArticleHistory(alias.qualifiedName, this)

  /**
   * Rename this table
   */
  override fun rename(name: String): BArticleHistory = BArticleHistory(DSL.name(name), null)

  /**
   * Rename this table
   */
  override fun rename(name: Name): BArticleHistory = BArticleHistory(name, null)

  /**
   * Rename this table
   */
  override fun rename(name: Table<*>): BArticleHistory = BArticleHistory(name.qualifiedName, null)

  /**
   * Create an inline derived table from this table
   */
  override fun where(condition: Condition?): BArticleHistory = BArticleHistory(qualifiedName, if (aliased()) this else null, condition)

  /**
   * Create an inline derived table from this table
   */
  override fun where(conditions: Collection<Condition>): BArticleHistory = where(DSL.and(conditions))

  /**
   * Create an inline derived table from this table
   */
  override fun where(vararg conditions: Condition?): BArticleHistory = where(DSL.and(*conditions))

  /**
   * Create an inline derived table from this table
   */
  override fun where(condition: Field<Boolean?>?): BArticleHistory = where(DSL.condition(condition))

  /**
   * Create an inline derived table from this table
   */
  @PlainSQL override fun where(condition: SQL): BArticleHistory = where(DSL.condition(condition))

  /**
   * Create an inline derived table from this table
   */
  @PlainSQL override fun where(
    @Stringly.SQL condition: String,
  ): BArticleHistory = where(DSL.condition(condition))

  /**
   * Create an inline derived table from this table
   */
  @PlainSQL override fun where(
    @Stringly.SQL condition: String,
    vararg binds: Any?,
  ): BArticleHistory = where(DSL.condition(condition, *binds))

  /**
   * Create an inline derived table from this table
   */
  @PlainSQL override fun where(
    @Stringly.SQL condition: String,
    vararg parts: QueryPart,
  ): BArticleHistory = where(DSL.condition(condition, *parts))

  /**
   * Create an inline derived table from this table
   */
  override fun whereExists(select: Select<*>): BArticleHistory = where(DSL.exists(select))

  /**
   * Create an inline derived table from this table
   */
  override fun whereNotExists(select: Select<*>): BArticleHistory = where(DSL.notExists(select))
}
