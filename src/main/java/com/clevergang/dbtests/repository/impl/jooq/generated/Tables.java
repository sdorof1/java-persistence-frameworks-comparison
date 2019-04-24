/*
 * This file is generated by jOOQ.
 */
package com.clevergang.dbtests.repository.impl.jooq.generated;


import com.clevergang.dbtests.repository.impl.jooq.generated.tables.Company;
import com.clevergang.dbtests.repository.impl.jooq.generated.tables.Department;
import com.clevergang.dbtests.repository.impl.jooq.generated.tables.Employee;
import com.clevergang.dbtests.repository.impl.jooq.generated.tables.Project;
import com.clevergang.dbtests.repository.impl.jooq.generated.tables.Projectemployee;
import com.clevergang.dbtests.repository.impl.jooq.generated.tables.RegisterEmployee;
import com.clevergang.dbtests.repository.impl.jooq.generated.tables.records.RegisterEmployeeRecord;

import java.math.BigDecimal;

import javax.annotation.Generated;

import org.jooq.Configuration;
import org.jooq.Field;
import org.jooq.Result;


/**
 * Convenience access to all tables in public
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.11.11"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Tables {

    /**
     * The table <code>public.company</code>.
     */
    public static final Company COMPANY = com.clevergang.dbtests.repository.impl.jooq.generated.tables.Company.COMPANY;

    /**
     * The table <code>public.department</code>.
     */
    public static final Department DEPARTMENT = com.clevergang.dbtests.repository.impl.jooq.generated.tables.Department.DEPARTMENT;

    /**
     * The table <code>public.employee</code>.
     */
    public static final Employee EMPLOYEE = com.clevergang.dbtests.repository.impl.jooq.generated.tables.Employee.EMPLOYEE;

    /**
     * The table <code>public.project</code>.
     */
    public static final Project PROJECT = com.clevergang.dbtests.repository.impl.jooq.generated.tables.Project.PROJECT;

    /**
     * The table <code>public.projectemployee</code>.
     */
    public static final Projectemployee PROJECTEMPLOYEE = com.clevergang.dbtests.repository.impl.jooq.generated.tables.Projectemployee.PROJECTEMPLOYEE;

    /**
     * The table <code>public.register_employee</code>.
     */
    public static final RegisterEmployee REGISTER_EMPLOYEE = com.clevergang.dbtests.repository.impl.jooq.generated.tables.RegisterEmployee.REGISTER_EMPLOYEE;

    /**
     * Call <code>public.register_employee</code>.
     */
    public static Result<RegisterEmployeeRecord> REGISTER_EMPLOYEE(Configuration configuration, String _Name, String _Surname, String _Email, BigDecimal _Salary, String _DepartmentName, String _CompanyName) {
        return configuration.dsl().selectFrom(com.clevergang.dbtests.repository.impl.jooq.generated.tables.RegisterEmployee.REGISTER_EMPLOYEE.call(_Name, _Surname, _Email, _Salary, _DepartmentName, _CompanyName)).fetch();
    }

    /**
     * Get <code>public.register_employee</code> as a table.
     */
    public static RegisterEmployee REGISTER_EMPLOYEE(String _Name, String _Surname, String _Email, BigDecimal _Salary, String _DepartmentName, String _CompanyName) {
        return com.clevergang.dbtests.repository.impl.jooq.generated.tables.RegisterEmployee.REGISTER_EMPLOYEE.call(_Name, _Surname, _Email, _Salary, _DepartmentName, _CompanyName);
    }

    /**
     * Get <code>public.register_employee</code> as a table.
     */
    public static RegisterEmployee REGISTER_EMPLOYEE(Field<String> _Name, Field<String> _Surname, Field<String> _Email, Field<BigDecimal> _Salary, Field<String> _DepartmentName, Field<String> _CompanyName) {
        return com.clevergang.dbtests.repository.impl.jooq.generated.tables.RegisterEmployee.REGISTER_EMPLOYEE.call(_Name, _Surname, _Email, _Salary, _DepartmentName, _CompanyName);
    }
}
