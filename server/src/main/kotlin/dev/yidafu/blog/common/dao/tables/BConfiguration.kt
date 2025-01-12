/*
 * This file is generated by jOOQ.
 */
package dev.yidafu.blog.common.dao.tables


import dev.yidafu.blog.common.dao.DefaultSchema
import dev.yidafu.blog.common.dao.keys.CONSTRAINT_2
import dev.yidafu.blog.common.dao.keys.UKR9EYOBGIOQOJSDRAYEEYMUNJH
import dev.yidafu.blog.common.dao.tables.records.BConfigurationRecord

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
open class BConfiguration(
    alias: Name,
    path: Table<out Record>?,
    childPath: ForeignKey<out Record, BConfigurationRecord>?,
    parentPath: InverseForeignKey<out Record, BConfigurationRecord>?,
    aliased: Table<BConfigurationRecord>?,
    parameters: Array<Field<*>?>?,
    where: Condition?
): TableImpl<BConfigurationRecord>(
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
         * The reference instance of <code>B_CONFIGURATION</code>
         */
        val B_CONFIGURATION: BConfiguration = BConfiguration()
    }

    /**
     * The class holding records for this type
     */
    override fun getRecordType(): Class<BConfigurationRecord> = BConfigurationRecord::class.java

    /**
     * The column <code>B_CONFIGURATION.ID</code>.
     */
    val ID: TableField<BConfigurationRecord, Long?> = createField(DSL.name("ID"), SQLDataType.BIGINT.nullable(false), this, "")

    /**
     * The column <code>B_CONFIGURATION.CREATED_AT</code>.
     */
    val CREATED_AT: TableField<BConfigurationRecord, LocalTime?> = createField(DSL.name("CREATED_AT"), SQLDataType.LOCALTIME, this, "")

    /**
     * The column <code>B_CONFIGURATION.UPDATED_AT</code>.
     */
    val UPDATED_AT: TableField<BConfigurationRecord, LocalTime?> = createField(DSL.name("UPDATED_AT"), SQLDataType.LOCALTIME, this, "")

    /**
     * The column <code>B_CONFIGURATION.CONFIG_KEY</code>.
     */
    val CONFIG_KEY: TableField<BConfigurationRecord, String?> = createField(DSL.name("CONFIG_KEY"), SQLDataType.VARCHAR(255), this, "")

    /**
     * The column <code>B_CONFIGURATION.CONFIG_VALUE</code>.
     */
    val CONFIG_VALUE: TableField<BConfigurationRecord, String?> = createField(DSL.name("CONFIG_VALUE"), SQLDataType.VARCHAR(255), this, "")

    private constructor(alias: Name, aliased: Table<BConfigurationRecord>?): this(alias, null, null, null, aliased, null, null)
    private constructor(alias: Name, aliased: Table<BConfigurationRecord>?, parameters: Array<Field<*>?>?): this(alias, null, null, null, aliased, parameters, null)
    private constructor(alias: Name, aliased: Table<BConfigurationRecord>?, where: Condition?): this(alias, null, null, null, aliased, null, where)

    /**
     * Create an aliased <code>B_CONFIGURATION</code> table reference
     */
    constructor(alias: String): this(DSL.name(alias))

    /**
     * Create an aliased <code>B_CONFIGURATION</code> table reference
     */
    constructor(alias: Name): this(alias, null)

    /**
     * Create a <code>B_CONFIGURATION</code> table reference
     */
    constructor(): this(DSL.name("B_CONFIGURATION"), null)
    override fun getSchema(): Schema? = if (aliased()) null else DefaultSchema.DEFAULT_SCHEMA
    override fun getPrimaryKey(): UniqueKey<BConfigurationRecord> = CONSTRAINT_2
    override fun getUniqueKeys(): List<UniqueKey<BConfigurationRecord>> = listOf(UKR9EYOBGIOQOJSDRAYEEYMUNJH)
    override fun `as`(alias: String): BConfiguration = BConfiguration(DSL.name(alias), this)
    override fun `as`(alias: Name): BConfiguration = BConfiguration(alias, this)
    override fun `as`(alias: Table<*>): BConfiguration = BConfiguration(alias.qualifiedName, this)

    /**
     * Rename this table
     */
    override fun rename(name: String): BConfiguration = BConfiguration(DSL.name(name), null)

    /**
     * Rename this table
     */
    override fun rename(name: Name): BConfiguration = BConfiguration(name, null)

    /**
     * Rename this table
     */
    override fun rename(name: Table<*>): BConfiguration = BConfiguration(name.qualifiedName, null)

    /**
     * Create an inline derived table from this table
     */
    override fun where(condition: Condition?): BConfiguration = BConfiguration(qualifiedName, if (aliased()) this else null, condition)

    /**
     * Create an inline derived table from this table
     */
    override fun where(conditions: Collection<Condition>): BConfiguration = where(DSL.and(conditions))

    /**
     * Create an inline derived table from this table
     */
    override fun where(vararg conditions: Condition?): BConfiguration = where(DSL.and(*conditions))

    /**
     * Create an inline derived table from this table
     */
    override fun where(condition: Field<Boolean?>?): BConfiguration = where(DSL.condition(condition))

    /**
     * Create an inline derived table from this table
     */
    @PlainSQL override fun where(condition: SQL): BConfiguration = where(DSL.condition(condition))

    /**
     * Create an inline derived table from this table
     */
    @PlainSQL override fun where(@Stringly.SQL condition: String): BConfiguration = where(DSL.condition(condition))

    /**
     * Create an inline derived table from this table
     */
    @PlainSQL override fun where(@Stringly.SQL condition: String, vararg binds: Any?): BConfiguration = where(DSL.condition(condition, *binds))

    /**
     * Create an inline derived table from this table
     */
    @PlainSQL override fun where(@Stringly.SQL condition: String, vararg parts: QueryPart): BConfiguration = where(DSL.condition(condition, *parts))

    /**
     * Create an inline derived table from this table
     */
    override fun whereExists(select: Select<*>): BConfiguration = where(DSL.exists(select))

    /**
     * Create an inline derived table from this table
     */
    override fun whereNotExists(select: Select<*>): BConfiguration = where(DSL.notExists(select))
}
