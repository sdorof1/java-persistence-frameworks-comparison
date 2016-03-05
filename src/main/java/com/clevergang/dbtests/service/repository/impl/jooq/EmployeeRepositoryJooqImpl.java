package com.clevergang.dbtests.service.repository.impl.jooq;

import java.math.BigDecimal;
import java.util.List;

import com.clevergang.dbtests.model.Employee;
import com.clevergang.dbtests.service.repository.EmployeeRepository;
import com.clevergang.dbtests.service.repository.impl.ImplBasedOn;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static com.clevergang.dbtests.service.repository.impl.RepoImplementation.JOOQ;
import static com.clevergang.dbtests.service.repository.impl.jooq.generated.Tables.EMPLOYEE;

/**
 * @author Bretislav Wajtr <bretislav.wajtr@clevergang.com>
 */
@Repository
@ImplBasedOn(JOOQ)
public class EmployeeRepositoryJooqImpl implements EmployeeRepository {
    private static final Logger logger = LoggerFactory.getLogger(CompanyRepositoryJooqImpl.class);

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    private DSLContext create;

    @Override
    public List<Employee> employeesWithSalaryGreaterThan(Integer minSalary) {
        logger.info("Looking for employeesWithSalaryGreaterThan using JOOQ");

        return create
            .select()
            .from(EMPLOYEE)
            .where(EMPLOYEE.SALARY.greaterThan(new BigDecimal(minSalary)))
            .fetchInto(Employee.class);
    }
}
