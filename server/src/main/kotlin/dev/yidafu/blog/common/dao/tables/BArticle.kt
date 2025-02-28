/*
 * This file is generated by jOOQ.
 */
package dev.yidafu.blog.common.dao.tables

import dev.yidafu.blog.common.dao.DefaultSchema
import dev.yidafu.blog.common.dao.keys.CONSTRAINT_C
import dev.yidafu.blog.common.dao.tables.records.BArticleRecord
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
open class BArticle(
  alias: Name,
  path: Table<out Record>?,
  childPath: ForeignKey<out Record, BArticleRecord>?,
  parentPath: InverseForeignKey<out Record, BArticleRecord>?,
  aliased: Table<BArticleRecord>?,
  parameters: Array<Field<*>?>?,
  where: Condition?,
) : TableImpl<BArticleRecord>(
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
     * The reference instance of <code>B_ARTICLE</code>
     */
    val B_ARTICLE: BArticle = BArticle()
  }

  /**
   * The class holding records for this type
   */
  override fun getRecordType(): Class<BArticleRecord> = BArticleRecord::class.java

  /**
   * The column <code>B_ARTICLE.ID</code>.
   */
  val ID: TableField<BArticleRecord, Long?> = createField(DSL.name("ID"), SQLDataType.BIGINT.nullable(false).identity(true), this, "")

  /**
   * The column <code>B_ARTICLE.CREATED_AT</code>.
   */
  val CREATED_AT: TableField<BArticleRecord, LocalDateTime?> =
    createField(
      DSL.name("CREATED_AT"),
      SQLDataType.LOCALDATETIME(6).defaultValue(DSL.field(DSL.raw("CURRENT_TIMESTAMP"), SQLDataType.LOCALDATETIME)),
      this,
      "",
    )

  /**
   * The column <code>B_ARTICLE.UPDATED_AT</code>.
   */
  val UPDATED_AT: TableField<BArticleRecord, LocalDateTime?> =
    createField(
      DSL.name("UPDATED_AT"),
      SQLDataType.LOCALDATETIME(6).defaultValue(DSL.field(DSL.raw("CURRENT_TIMESTAMP"), SQLDataType.LOCALDATETIME)),
      this,
      "",
    )

  /**
   * The column <code>B_ARTICLE.CONTENT</code>.
   */
  val CONTENT: TableField<BArticleRecord, String?> = createField(DSL.name("CONTENT"), SQLDataType.VARCHAR(1000000000), this, "")

  /**
   * The column <code>B_ARTICLE.COVER</code>.
   */
  val COVER: TableField<BArticleRecord, String?> = createField(DSL.name("COVER"), SQLDataType.VARCHAR(255), this, "")

  /**
   * The column <code>B_ARTICLE.HASH</code>.
   */
  val HASH: TableField<BArticleRecord, String?> = createField(DSL.name("HASH"), SQLDataType.VARCHAR(255), this, "")

  /**
   * The column <code>B_ARTICLE.HTML</code>.
   */
  val HTML: TableField<BArticleRecord, String?> = createField(DSL.name("HTML"), SQLDataType.VARCHAR(1000000000), this, "")

  /**
   * The column <code>B_ARTICLE.IDENTIFIER</code>.
   */
  val IDENTIFIER: TableField<BArticleRecord, String?> = createField(DSL.name("IDENTIFIER"), SQLDataType.VARCHAR(255), this, "")

  /**
   * The column <code>B_ARTICLE.SERIES</code>.
   */
  val SERIES: TableField<BArticleRecord, String?> = createField(DSL.name("SERIES"), SQLDataType.VARCHAR(255), this, "")

  /**
   * The column <code>B_ARTICLE.SOURCE_TYPE</code>.
   */
  val SOURCE_TYPE: TableField<BArticleRecord, String?> = createField(DSL.name("SOURCE_TYPE"), SQLDataType.VARCHAR(255), this, "")

  /**
   * The column <code>B_ARTICLE.STATUS</code>.
   */
  val STATUS: TableField<BArticleRecord, Int?> = createField(DSL.name("STATUS"), SQLDataType.INTEGER, this, "")

  /**
   * The column <code>B_ARTICLE.SUMMARY</code>.
   */
  val SUMMARY: TableField<BArticleRecord, String?> = createField(DSL.name("SUMMARY"), SQLDataType.VARCHAR(255), this, "")

  /**
   * The column <code>B_ARTICLE.TITLE</code>.
   */
  val TITLE: TableField<BArticleRecord, String?> = createField(DSL.name("TITLE"), SQLDataType.VARCHAR(255), this, "")

  private constructor(alias: Name, aliased: Table<BArticleRecord>?) : this(alias, null, null, null, aliased, null, null)
  private constructor(
    alias: Name,
    aliased: Table<BArticleRecord>?,
    parameters: Array<Field<*>?>?,
  ) : this(alias, null, null, null, aliased, parameters, null)
  private constructor(
    alias: Name,
    aliased: Table<BArticleRecord>?,
    where: Condition?,
  ) : this(alias, null, null, null, aliased, null, where)

  /**
   * Create an aliased <code>B_ARTICLE</code> table reference
   */
  constructor(alias: String) : this(DSL.name(alias))

  /**
   * Create an aliased <code>B_ARTICLE</code> table reference
   */
  constructor(alias: Name) : this(alias, null)

  /**
   * Create a <code>B_ARTICLE</code> table reference
   */
  constructor() : this(DSL.name("B_ARTICLE"), null)

  override fun getSchema(): Schema? = if (aliased()) null else DefaultSchema.DEFAULT_SCHEMA

  override fun getIdentity(): Identity<BArticleRecord, Long?> = super.getIdentity() as Identity<BArticleRecord, Long?>

  override fun getPrimaryKey(): UniqueKey<BArticleRecord> = CONSTRAINT_C

  override fun `as`(alias: String): BArticle = BArticle(DSL.name(alias), this)

  override fun `as`(alias: Name): BArticle = BArticle(alias, this)

  override fun `as`(alias: Table<*>): BArticle = BArticle(alias.qualifiedName, this)

  /**
   * Rename this table
   */
  override fun rename(name: String): BArticle = BArticle(DSL.name(name), null)

  /**
   * Rename this table
   */
  override fun rename(name: Name): BArticle = BArticle(name, null)

  /**
   * Rename this table
   */
  override fun rename(name: Table<*>): BArticle = BArticle(name.qualifiedName, null)

  /**
   * Create an inline derived table from this table
   */
  override fun where(condition: Condition?): BArticle = BArticle(qualifiedName, if (aliased()) this else null, condition)

  /**
   * Create an inline derived table from this table
   */
  override fun where(conditions: Collection<Condition>): BArticle = where(DSL.and(conditions))

  /**
   * Create an inline derived table from this table
   */
  override fun where(vararg conditions: Condition?): BArticle = where(DSL.and(*conditions))

  /**
   * Create an inline derived table from this table
   */
  override fun where(condition: Field<Boolean?>?): BArticle = where(DSL.condition(condition))

  /**
   * Create an inline derived table from this table
   */
  @PlainSQL override fun where(condition: SQL): BArticle = where(DSL.condition(condition))

  /**
   * Create an inline derived table from this table
   */
  @PlainSQL override fun where(
    @Stringly.SQL condition: String,
  ): BArticle = where(DSL.condition(condition))

  /**
   * Create an inline derived table from this table
   */
  @PlainSQL override fun where(
    @Stringly.SQL condition: String,
    vararg binds: Any?,
  ): BArticle = where(DSL.condition(condition, *binds))

  /**
   * Create an inline derived table from this table
   */
  @PlainSQL override fun where(
    @Stringly.SQL condition: String,
    vararg parts: QueryPart,
  ): BArticle = where(DSL.condition(condition, *parts))

  /**
   * Create an inline derived table from this table
   */
  override fun whereExists(select: Select<*>): BArticle = where(DSL.exists(select))

  /**
   * Create an inline derived table from this table
   */
  override fun whereNotExists(select: Select<*>): BArticle = where(DSL.notExists(select))
}
