/*
 * This file is generated by jOOQ.
 */
package com.clevergang.dbtests.repository.impl.jooq.generated.tables;


import com.clevergang.dbtests.repository.impl.jooq.generated.Indexes;
import com.clevergang.dbtests.repository.impl.jooq.generated.Keys;
import com.clevergang.dbtests.repository.impl.jooq.generated.Public;
import com.clevergang.dbtests.repository.impl.jooq.generated.tables.records.ProjectemployeeRecord;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Index;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.11.11"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Projectemployee extends TableImpl<ProjectemployeeRecord> {

    private static final long serialVersionUID = -1538179083;

    /**
     * The reference instance of <code>public.projectemployee</code>
     */
    public static final Projectemployee PROJECTEMPLOYEE = new Projectemployee();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<ProjectemployeeRecord> getRecordType() {
        return ProjectemployeeRecord.class;
    }

    /**
     * The column <code>public.projectemployee.project_pid</code>.
     */
    public final TableField<ProjectemployeeRecord, Integer> PROJECT_PID = createField("project_pid", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>public.projectemployee.employee_pid</code>.
     */
    public final TableField<ProjectemployeeRecord, Integer> EMPLOYEE_PID = createField("employee_pid", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * Create a <code>public.projectemployee</code> table reference
     */
    public Projectemployee() {
        this(DSL.name("projectemployee"), null);
    }

    /**
     * Create an aliased <code>public.projectemployee</code> table reference
     */
    public Projectemployee(String alias) {
        this(DSL.name(alias), PROJECTEMPLOYEE);
    }

    /**
     * Create an aliased <code>public.projectemployee</code> table reference
     */
    public Projectemployee(Name alias) {
        this(alias, PROJECTEMPLOYEE);
    }

    private Projectemployee(Name alias, Table<ProjectemployeeRecord> aliased) {
        this(alias, aliased, null);
    }

    private Projectemployee(Name alias, Table<ProjectemployeeRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
    }

    public <O extends Record> Projectemployee(Table<O> child, ForeignKey<O, ProjectemployeeRecord> key) {
        super(child, key, PROJECTEMPLOYEE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Schema getSchema() {
        return Public.PUBLIC;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.PROJECTEMPLOYEE_PKEY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<ProjectemployeeRecord> getPrimaryKey() {
        return Keys.PROJECTEMPLOYEE_PKEY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<ProjectemployeeRecord>> getKeys() {
        return Arrays.<UniqueKey<ProjectemployeeRecord>>asList(Keys.PROJECTEMPLOYEE_PKEY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ForeignKey<ProjectemployeeRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<ProjectemployeeRecord, ?>>asList(Keys.PROJECTEMPLOYEE__PROJECTEMPLOYEE_PROJECT_PID_FKEY, Keys.PROJECTEMPLOYEE__PROJECTEMPLOYEE_EMPLOYEE_PID_FKEY);
    }

    public Project project() {
        return new Project(this, Keys.PROJECTEMPLOYEE__PROJECTEMPLOYEE_PROJECT_PID_FKEY);
    }

    public Employee employee() {
        return new Employee(this, Keys.PROJECTEMPLOYEE__PROJECTEMPLOYEE_EMPLOYEE_PID_FKEY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Projectemployee as(String alias) {
        return new Projectemployee(DSL.name(alias), this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Projectemployee as(Name alias) {
        return new Projectemployee(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Projectemployee rename(String name) {
        return new Projectemployee(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Projectemployee rename(Name name) {
        return new Projectemployee(name, null);
    }
}
