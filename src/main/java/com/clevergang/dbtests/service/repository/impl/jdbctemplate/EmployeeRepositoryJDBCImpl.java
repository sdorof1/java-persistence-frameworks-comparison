package com.clevergang.dbtests.service.repository.impl.jdbctemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.clevergang.dbtests.model.Employee;
import com.clevergang.dbtests.service.repository.EmployeeRepository;
import com.clevergang.dbtests.service.repository.impl.ImplBasedOn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import static com.clevergang.dbtests.service.repository.impl.RepoImplementation.JDBCTEMPLATE;

/**
 * @author Bretislav Wajtr <bretislav.wajtr@clevergang.com>
 */
@Repository
@ImplBasedOn(JDBCTEMPLATE)
public class EmployeeRepositoryJDBCImpl implements EmployeeRepository {

    private static final Logger logger = LoggerFactory.getLogger(CompanyRepositoryJDBCImpl.class);

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public List<Employee> employeesWithSalaryGreaterThan(Integer minSalary) {
        logger.info("Looking for employeesWithSalaryGreaterThan using JDBCTemplate");

        String query = "SELECT * " +
            "           FROM employee" +
            "           WHERE salary > :salary";

        Map<String, Object> params = new HashMap<>();
        params.put("salary", minSalary);

        return jdbcTemplate.query(query, params, new BeanPropertyRowMapper<>(Employee.class));
    }
}
