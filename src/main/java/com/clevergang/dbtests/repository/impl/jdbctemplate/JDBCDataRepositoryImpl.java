package com.clevergang.dbtests.repository.impl.jdbctemplate;

import com.clevergang.dbtests.repository.api.DataRepository;
import com.clevergang.dbtests.repository.api.data.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Spring JDBCTemplate implementation of the DataRepository
 *
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

        String query;
        query = "SELECT pid, address, name " +
                "FROM company " +
                "WHERE pid = :pid";

        Map<String, Object> params = new HashMap<>();
        params.put("pid", pid);

        RowMapper<Company> mapper = (rs, rowNum) -> {
            Company row = new Company();
            row.setPid(rs.getInt("pid"));
            row.setAddress(rs.getString("address"));
            row.setName(rs.getString("name"));
            return row;
        };

        Company company = jdbcTemplate.queryForObject(query, params, mapper);

        logger.info("Found company: " + company);

        return company;
    }

    @Override
    public Company findCompanyUsingSimpleStaticStatement(Integer pid) {
        String query;
        query = "SELECT pid, address, name " +
                "FROM company " +
                "WHERE pid = " + pid;

        RowMapper<Company> mapper = (rs, rowNum) -> {
            Company row = new Company();
            row.setPid(rs.getInt("pid"));
            row.setAddress(rs.getString("address"));
            row.setName(rs.getString("name"));
            return row;
        };

        // In Spring's JDBCTemplate it's pretty easy to execute the query using static Statement (not the PreparedStatement):
        // In this DAO we autowired NamedParameterJdbcTemplate, which does not allow static statements, but this class
        // is just a wrapper around original JdbcTemplate which allows static statements -> so we used getJdbcOperations()
        // to get such original object and then just used one of it's methods which internally calls the static
        // statement (see javadoc for each JdbcOperations#query*() method)
        Company company = jdbcTemplate.getJdbcOperations().queryForObject(query, mapper);

        logger.info("Found company: " + company);

        return company;
    }

    @Override
    public Department findDepartment(Integer pid) {
        String query;
        query = "SELECT pid, company_pid, name" +
                "           FROM department " +
                "           WHERE pid = :pid";

        Map<String, Object> params = new HashMap<>();
        params.put("pid", pid);

        // using BeanPropertyRowMapper is easier, but with much worse performance than custom RowMapper
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

        // using BeanPropertyRowMapper is easier, but with much worse performance than custom RowMapper
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
    public List<Integer> insertProjects(List<Project> projects) {
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

    @Override
    public List<ProjectsWithCostsGreaterThanOutput> getProjectsWithCostsGreaterThan(int totalCostBoundary) {
        String query;
        query = "WITH project_info AS (\n" +
                "    SELECT project.pid project_pid, project.name project_name, salary monthly_cost, company.name company_name\n" +
                "    FROM project\n" +
                "      JOIN projectemployee ON project.pid = projectemployee.project_pid\n" +
                "      JOIN employee ON projectemployee.employee_pid = employee.pid\n" +
                "      LEFT JOIN department ON employee.department_pid = department.pid\n" +
                "      LEFT JOIN company ON department.company_pid = company.pid\n" +
                "),\n" +
                "project_cost AS (\n" +
                "    SELECT project_pid, sum(monthly_cost) total_cost\n" +
                "    FROM project_info GROUP BY project_pid\n" +
                ")\n" +
                "SELECT project_name, total_cost, company_name, sum(monthly_cost) company_cost FROM project_info\n" +
                "  JOIN project_cost USING (project_pid)\n" +
                "WHERE total_cost > :totalCostBoundary\n" +
                "GROUP BY project_name, total_cost, company_name";

        Map<String, Object> params = new HashMap<>();
        params.put("totalCostBoundary", totalCostBoundary);

        RowMapper<ProjectsWithCostsGreaterThanOutput> mapper = (rs, rowNum) -> {
            ProjectsWithCostsGreaterThanOutput row = new ProjectsWithCostsGreaterThanOutput();
            row.setProjectName(rs.getString("project_name"));
            row.setTotalCost(rs.getBigDecimal("total_cost"));
            row.setCompanyName(rs.getString("company_name"));
            row.setCompanyCost(rs.getBigDecimal("company_cost"));
            return row;
        };

        return jdbcTemplate.query(query, params, mapper);
    }

    @Override
    public RegisterEmployeeOutput callRegisterEmployee(String name, String surname, String email, BigDecimal salary, String departmentName, String companyName) {
        String query;
        query = "SELECT employee_id, department_id, company_id FROM register_employee(\n" +
                "  _name := :name, \n" +
                "  _surname := :surname, \n" +
                "  _email := :email, \n" +
                "  _salary := :salary, \n" +
                "  _department_name := :departmentName, \n" +
                "  _company_name := :companyName\n" +
                ")";

        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("surname", surname);
        params.put("email", email);
        params.put("salary", salary);
        params.put("departmentName", departmentName);
        params.put("companyName", companyName);

        RowMapper<RegisterEmployeeOutput> mapper = (rs, rowNum) -> {
            RegisterEmployeeOutput row = new RegisterEmployeeOutput();
            row.setEmployeePid(rs.getInt("employee_id"));
            row.setDepartmentPid(rs.getInt("department_id"));
            row.setCompanyPid(rs.getInt("company_id"));
            return row;
        };

        return jdbcTemplate.queryForObject(query, params, mapper);
    }

    @Override
    public List<Department> findDepartmentsOfCompany(Company company) {
        String query = "SELECT pid, company_pid, name" +
                "           FROM department " +
                "           WHERE company_pid = :pid" +
                "           ORDER BY pid";

        Map<String, Object> params = new HashMap<>();
        params.put("pid", company.getPid());

        return jdbcTemplate.query(query, params, new BeanPropertyRowMapper<>(Department.class));
    }

    @Override
    public void deleteDepartments(List<Department> departmentsToDelete) {
        List<Integer> ids = departmentsToDelete.stream()
                .map(Department::getPid)
                .collect(Collectors.toList());

        Map<String, Object> params = new HashMap<>();
        params.put("pids", ids);

        String updateStatement = "DELETE FROM department where pid IN (:pids)";

        jdbcTemplate.update(updateStatement, params);
    }

    @Override
    public void updateDepartments(List<Department> departmentsToUpdate) {
        //language=PostgreSQL
        String updateStatement = "UPDATE department SET " +
                "company_pid = :company_pid, " +
                "name = :name " +
                "WHERE pid = :pid";

        batchUpdateDepartments(departmentsToUpdate, updateStatement);
    }

    @Override
    public void insertDepartments(List<Department> departmentsToInsert) {
        //language=PostgreSQL
        String insertStatement = " INSERT INTO department (company_pid, name) " +
                " VALUES (:company_pid, :name)";

        batchUpdateDepartments(departmentsToInsert, insertStatement);
    }

    private void batchUpdateDepartments(List<Department> departmentsToInsert, String statement) {
        MapSqlParameterSource[] paramsList = departmentsToInsert
                .stream()
                .map(department -> {
                    MapSqlParameterSource params = new MapSqlParameterSource();
                    params.addValue("pid", department.getPid());
                    params.addValue("company_pid", department.getCompanyPid());
                    params.addValue("name", department.getName());
                    return params;
                })
                .toArray(MapSqlParameterSource[]::new);

        jdbcTemplate.batchUpdate(statement, paramsList);
    }

}
