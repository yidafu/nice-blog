/*
 * This file is generated by jOOQ.
 */
package dev.yidafu.blog.common.dao.tables


import dev.yidafu.blog.common.dao.DefaultSchema
import dev.yidafu.blog.common.dao.keys.CONSTRAINT_3
import dev.yidafu.blog.common.dao.keys.UK_1D63EUQ1SRIUU4GWDPQHR6559
import dev.yidafu.blog.common.dao.tables.records.BUserTokenRecord

import java.time.LocalDate
import java.time.LocalTime

import kotlin.collections.Collection
import kotlin.collections.List

import org.jooq.Condition
import org.jooq.Field
import org.jooq.ForeignKey
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


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
open class BUserToken(
    alias: Name,
    path: Table<out Record>?,
    childPath: ForeignKey<out Record, BUserTokenRecord>?,
    parentPath: InverseForeignKey<out Record, BUserTokenRecord>?,
    aliased: Table<BUserTokenRecord>?,
    parameters: Array<Field<*>?>?,
    where: Condition?
): TableImpl<BUserTokenRecord>(
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
         * The reference instance of <code>B_USER_TOKEN</code>
         */
        val B_USER_TOKEN: BUserToken = BUserToken()
    }

    /**
     * The class holding records for this type
     */
    override fun getRecordType(): Class<BUserTokenRecord> = BUserTokenRecord::class.java

    /**
     * The column <code>B_USER_TOKEN.ID</code>.
     */
    val ID: TableField<BUserTokenRecord, Long?> = createField(DSL.name("ID"), SQLDataType.BIGINT.nullable(false), this, "")

    /**
     * The column <code>B_USER_TOKEN.CREATED_AT</code>.
     */
    val CREATED_AT: TableField<BUserTokenRecord, LocalTime?> = createField(DSL.name("CREATED_AT"), SQLDataType.LOCALTIME, this, "")

    /**
     * The column <code>B_USER_TOKEN.CREATED_BY</code>.
     */
    val CREATED_BY: TableField<BUserTokenRecord, LocalDate?> = createField(DSL.name("CREATED_BY"), SQLDataType.LOCALDATE.nullable(false), this, "")

    /**
     * The column <code>B_USER_TOKEN.DESCRIPTION</code>.
     */
    val DESCRIPTION: TableField<BUserTokenRecord, String?> = createField(DSL.name("DESCRIPTION"), SQLDataType.VARCHAR(255), this, "")

    /**
     * The column <code>B_USER_TOKEN.EXPIRES_AT</code>.
     */
    val EXPIRES_AT: TableField<BUserTokenRecord, LocalDate?> = createField(DSL.name("EXPIRES_AT"), SQLDataType.LOCALDATE.nullable(false), this, "")

    /**
     * The column <code>B_USER_TOKEN.NAME</code>.
     */
    val NAME: TableField<BUserTokenRecord, String?> = createField(DSL.name("NAME"), SQLDataType.VARCHAR(255), this, "")

    /**
     * The column <code>B_USER_TOKEN.TOKEN</code>.
     */
    val TOKEN: TableField<BUserTokenRecord, String?> = createField(DSL.name("TOKEN"), SQLDataType.VARCHAR(32).nullable(false), this, "")

    /**
     * The column <code>B_USER_TOKEN.UPDATED_AT</code>.
     */
    val UPDATED_AT: TableField<BUserTokenRecord, LocalTime?> = createField(DSL.name("UPDATED_AT"), SQLDataType.LOCALTIME, this, "")

    private constructor(alias: Name, aliased: Table<BUserTokenRecord>?): this(alias, null, null, null, aliased, null, null)
    private constructor(alias: Name, aliased: Table<BUserTokenRecord>?, parameters: Array<Field<*>?>?): this(alias, null, null, null, aliased, parameters, null)
    private constructor(alias: Name, aliased: Table<BUserTokenRecord>?, where: Condition?): this(alias, null, null, null, aliased, null, where)

    /**
     * Create an aliased <code>B_USER_TOKEN</code> table reference
     */
    constructor(alias: String): this(DSL.name(alias))

    /**
     * Create an aliased <code>B_USER_TOKEN</code> table reference
     */
    constructor(alias: Name): this(alias, null)

    /**
     * Create a <code>B_USER_TOKEN</code> table reference
     */
    constructor(): this(DSL.name("B_USER_TOKEN"), null)
    override fun getSchema(): Schema? = if (aliased()) null else DefaultSchema.DEFAULT_SCHEMA
    override fun getPrimaryKey(): UniqueKey<BUserTokenRecord> = CONSTRAINT_3
    override fun getUniqueKeys(): List<UniqueKey<BUserTokenRecord>> = listOf(UK_1D63EUQ1SRIUU4GWDPQHR6559)
    override fun `as`(alias: String): BUserToken = BUserToken(DSL.name(alias), this)
    override fun `as`(alias: Name): BUserToken = BUserToken(alias, this)
    override fun `as`(alias: Table<*>): BUserToken = BUserToken(alias.qualifiedName, this)

    /**
     * Rename this table
     */
    override fun rename(name: String): BUserToken = BUserToken(DSL.name(name), null)

    /**
     * Rename this table
     */
    override fun rename(name: Name): BUserToken = BUserToken(name, null)

    /**
     * Rename this table
     */
    override fun rename(name: Table<*>): BUserToken = BUserToken(name.qualifiedName, null)

    /**
     * Create an inline derived table from this table
     */
    override fun where(condition: Condition?): BUserToken = BUserToken(qualifiedName, if (aliased()) this else null, condition)

    /**
     * Create an inline derived table from this table
     */
    override fun where(conditions: Collection<Condition>): BUserToken = where(DSL.and(conditions))

    /**
     * Create an inline derived table from this table
     */
    override fun where(vararg conditions: Condition?): BUserToken = where(DSL.and(*conditions))

    /**
     * Create an inline derived table from this table
     */
    override fun where(condition: Field<Boolean?>?): BUserToken = where(DSL.condition(condition))

    /**
     * Create an inline derived table from this table
     */
    @PlainSQL override fun where(condition: SQL): BUserToken = where(DSL.condition(condition))

    /**
     * Create an inline derived table from this table
     */
    @PlainSQL override fun where(@Stringly.SQL condition: String): BUserToken = where(DSL.condition(condition))

    /**
     * Create an inline derived table from this table
     */
    @PlainSQL override fun where(@Stringly.SQL condition: String, vararg binds: Any?): BUserToken = where(DSL.condition(condition, *binds))

    /**
     * Create an inline derived table from this table
     */
    @PlainSQL override fun where(@Stringly.SQL condition: String, vararg parts: QueryPart): BUserToken = where(DSL.condition(condition, *parts))

    /**
     * Create an inline derived table from this table
     */
    override fun whereExists(select: Select<*>): BUserToken = where(DSL.exists(select))

    /**
     * Create an inline derived table from this table
     */
    override fun whereNotExists(select: Select<*>): BUserToken = where(DSL.notExists(select))
}
