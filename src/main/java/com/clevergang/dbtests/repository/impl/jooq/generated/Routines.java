/**
 * This class is generated by jOOQ
 */
package com.clevergang.dbtests.repository.impl.jooq.generated;


import com.clevergang.dbtests.repository.impl.jooq.generated.routines.RegisterEmployee;
import org.jooq.Configuration;

import javax.annotation.Generated;
import java.math.BigDecimal;


/**
 * Convenience access to all stored procedures and functions in public
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.8.4"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Routines {

    /**
     * Call <code>public.register_employee</code>
     */
    public static RegisterEmployee registerEmployee(Configuration configuration, String _Name, String _Surname, String _Email, BigDecimal _Salary, String _DepartmentName, String _CompanyName) {
        RegisterEmployee p = new RegisterEmployee();
        p.set_Name(_Name);
        p.set_Surname(_Surname);
        p.set_Email(_Email);
        p.set_Salary(_Salary);
        p.set_DepartmentName(_DepartmentName);
        p.set_CompanyName(_CompanyName);

        p.execute(configuration);
        return p;
    }
}