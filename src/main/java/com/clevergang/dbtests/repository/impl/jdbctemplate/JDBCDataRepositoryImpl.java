package com.clevergang.dbtests.repository.impl.jdbctemplate;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.clevergang.dbtests.model.Company;
import com.clevergang.dbtests.model.Department;
import com.clevergang.dbtests.model.Employee;
import com.clevergang.dbtests.model.Project;
import com.clevergang.dbtests.repository.DataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

/**
 * @author Bretislav Wajtr <bretislav.wajtr@clevergang.com>
 */
@Repository
public class JDBCDataRepositoryImpl implements DataRepository {
    private static final Logger logger = LoggerFactory.getLogger(JDBCDataRepositoryImpl.class);

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public Company findCompany(Integer pid) {
        logger.info("Finding Company by ID using JDBCTemplate");

        String query = "SELECT pid, address, name " +
            "           FROM company " +
            "           WHERE pid = :pid";

        Map<String, Object> params = new HashMap<>();
        params.put("pid", pid);

        Company company = jdbcTemplate.queryForObject(query, params, new BeanPropertyRowMapper<>(Company.class));

        logger.info("Found company: " + company);

        return company;
    }

    @Override
    public Department findDepartment(Integer pid) {
        String query = "SELECT pid, company_pid, name" +
            "           FROM department " +
            "           WHERE pid = :pid";

        Map<String, Object> params = new HashMap<>();
        params.put("pid", pid);

        return jdbcTemplate.queryForObject(query, params, new BeanPropertyRowMapper<>(Department.class));
    }

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

    @Override
    public Integer insertProject(Project project) {
        logger.info("Inserting project using JDBCTemplate");

        String insertStatement = " INSERT INTO project (name, datestarted) " +
            " VALUES (:name, :datestarted)";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", project.getName());
        params.addValue("datestarted", Date.valueOf(project.getDate()));

        KeyHolder generatedKey = new GeneratedKeyHolder();

        jdbcTemplate.update(insertStatement, params, generatedKey);

        return (Integer) generatedKey.getKeys().get("pid");
    }

    @Override
    public List<Integer> insertAllProjects(List<Project> projects) {
        logger.info("Batch inserting projects using JDBCTemplate");

        String insertStatement = " INSERT INTO project (name, datestarted) " +
            " VALUES (:name, :datestarted)";

        MapSqlParameterSource[] paramsList = projects
            .stream()
            .map(project -> {
                MapSqlParameterSource params = new MapSqlParameterSource();
                params.addValue("name", project.getName());
                params.addValue("datestarted", Date.valueOf(project.getDate()));
                return params;
            })
            .toArray(MapSqlParameterSource[]::new);

        jdbcTemplate.batchUpdate(insertStatement, paramsList);

        // FIXME JDBCTemplate cannot return IDs for every row after batch update!!
        return null;
    }

    @Override
    public void updateEmployee(Employee employeeToUpdate) {
        logger.info("Updating employee using JDBC Template");

        String updateStatement = " UPDATE EMPLOYEE SET " +
            " department_pid = :department_pid, " +
            " name = :name," +
            " surname = :surname," +
            " email = :email," +
            " salary = :salary" +
            " WHERE pid = :pid";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("department_pid", employeeToUpdate.getDepartmentPid());
        params.addValue("name", employeeToUpdate.getName());
        params.addValue("surname", employeeToUpdate.getSurname());
        params.addValue("email", employeeToUpdate.getEmail());
        params.addValue("salary", employeeToUpdate.getSalary());
        params.addValue("pid", employeeToUpdate.getPid());

        jdbcTemplate.update(updateStatement, params);
    }
}
