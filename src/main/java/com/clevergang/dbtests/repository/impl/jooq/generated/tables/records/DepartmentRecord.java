/*
 * This file is generated by jOOQ.
 */
package com.clevergang.dbtests.repository.impl.jooq.generated.tables.records;


import com.clevergang.dbtests.repository.impl.jooq.generated.tables.Department;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record3;
import org.jooq.Row3;
import org.jooq.impl.UpdatableRecordImpl;


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
public class DepartmentRecord extends UpdatableRecordImpl<DepartmentRecord> implements Record3<Integer, Integer, String> {

    private static final long serialVersionUID = 1770509226;

    /**
     * Setter for <code>public.department.pid</code>.
     */
    public void setPid(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.department.pid</code>.
     */
    public Integer getPid() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>public.department.company_pid</code>.
     */
    public void setCompanyPid(Integer value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.department.company_pid</code>.
     */
    public Integer getCompanyPid() {
        return (Integer) get(1);
    }

    /**
     * Setter for <code>public.department.name</code>.
     */
    public void setName(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>public.department.name</code>.
     */
    public String getName() {
        return (String) get(2);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Record1<Integer> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record3 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row3<Integer, Integer, String> fieldsRow() {
        return (Row3) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row3<Integer, Integer, String> valuesRow() {
        return (Row3) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field1() {
        return Department.DEPARTMENT.PID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field2() {
        return Department.DEPARTMENT.COMPANY_PID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field3() {
        return Department.DEPARTMENT.NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer component1() {
        return getPid();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer component2() {
        return getCompanyPid();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component3() {
        return getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value1() {
        return getPid();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value2() {
        return getCompanyPid();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value3() {
        return getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DepartmentRecord value1(Integer value) {
        setPid(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DepartmentRecord value2(Integer value) {
        setCompanyPid(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DepartmentRecord value3(String value) {
        setName(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DepartmentRecord values(Integer value1, Integer value2, String value3) {
        value1(value1);
        value2(value2);
        value3(value3);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached DepartmentRecord
     */
    public DepartmentRecord() {
        super(Department.DEPARTMENT);
    }

    /**
     * Create a detached, initialised DepartmentRecord
     */
    public DepartmentRecord(Integer pid, Integer companyPid, String name) {
        super(Department.DEPARTMENT);

        set(0, pid);
        set(1, companyPid);
        set(2, name);
    }
}
