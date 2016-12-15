package com.clevergang.dbtests.repository.impl.jdbctemplate;

import com.clevergang.dbtests.repository.api.DataRepository;
import com.clevergang.dbtests.repository.api.data.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Spring JDBCTemplate implementation of the DataRepository
 *
 * @author Bretislav Wajtr
 */
@Repository
public class JDBCDataRepositoryImpl implements DataRepository {
    private static final Logger logger = LoggerFactory.getLogger(JDBCDataRepositoryImpl.class);

    private final NamedParameterJdbcTemplate jdbcTemplate;

    // NOTE: I would recommend to handle creation and caching of SimpleJdbcInsert instances like depicted here, for two reasons:
    // 1. In order to SimpleJdbcInsert to be effective, it cannot be created for each call - remember that during first use of
    // SimpleJdbcInsert, metadata for the table are fetched from database and this costs some time. So best way how to use SimpleJdbcInsert
    // is to reuse single instance.
    // 2. I wouldn't put instances of SimpleJdbcInsert to some external cache, but, like here, tie the instance of SimpleJdbcInsert with DAO bean.
    // That way it's ensured, that when the DAO bean is recreated so are be recreated all SimpleJdbcInsert instances the DAO bean depends on...
    private final SimpleJdbcInsert insertIntoProject;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    public JDBCDataRepositoryImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertIntoProject = new SimpleJdbcInsert((JdbcTemplate) jdbcTemplate.getJdbcOperations())
                .withTableName("project")
                .usingGeneratedKeyColumns("pid");
    }

    @Override
    public Company findCompany(Integer pid) {
        logger.info("Finding Company by ID using JDBCTemplate");

        String query = "SELECT pid, address, name " +
                "FROM company " +
                "WHERE pid = :pid";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("pid", pid);

        /*
         * Note that you can use BeanPropertyRowMapper instead (see findDepartment())
         */
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
        String query = "SELECT pid, company_pid, name" +
                "           FROM department " +
                "           WHERE pid = :pid";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("pid", pid);

        // using BeanPropertyRowMapper is easier, but with much worse performance than custom RowMapper
        return jdbcTemplate.queryForObject(query, params, BeanPropertyRowMapper.newInstance(Department.class));
    }

    @Override
    public List<Employee> employeesWithSalaryGreaterThan(Integer minSalary) {
        logger.info("Looking for employeesWithSalaryGreaterThan using JDBCTemplate");

        String query = "SELECT * " +
                "           FROM employee" +
                "           WHERE salary > :salary";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("salary", minSalary);

        // using BeanPropertyRowMapper is easier, but with much worse performance than custom RowMapper
        return jdbcTemplate.query(query, params, BeanPropertyRowMapper.newInstance(Employee.class));
    }

    @Override
    public Integer insertProject(Project project) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", project.getName())
                .addValue("datestarted", project.getDate());

        return insertIntoProject.executeAndReturnKey(params).intValue();
    }

    @Override
    public List<Integer> insertProjects(List<Project> projects) {
        MapSqlParameterSource[] paramsList = projects
                .stream()
                .map(project -> new MapSqlParameterSource()
                        .addValue("name", project.getName())
                        .addValue("datestarted", project.getDate()))
                .toArray(MapSqlParameterSource[]::new);

        // NOTE: Here we used SimpleJdbcInsert to insert the rows, but you can also use more traditional way
        // by specifying the statment to be executed (handy when you need to do some customizations of the statement).
        // See insertDepartments() method for an example.
        insertIntoProject.executeBatch(paramsList);

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

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("department_pid", employeeToUpdate.getDepartmentPid())
                .addValue("name", employeeToUpdate.getName())
                .addValue("surname", employeeToUpdate.getSurname())
                .addValue("email", employeeToUpdate.getEmail())
                .addValue("salary", employeeToUpdate.getSalary())
                .addValue("pid", employeeToUpdate.getPid());

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
                "GROUP BY project_name, total_cost, company_name\n" +
                "ORDER BY company_name";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("totalCostBoundary", totalCostBoundary);

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
    public Employee findEmployee(Integer pid) {
        String query;
        query = "SELECT pid, email, name, salary, surname, department_pid AS departmentPid FROM employee WHERE pid = :pid";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("pid", pid);

        // using BeanPropertyRowMapper is easier, but with much worse performance than custom RowMapper
        return jdbcTemplate.queryForObject(query, params, BeanPropertyRowMapper.newInstance(Employee.class));
    }

    @Override
    public RegisterEmployeeOutput callRegisterEmployee(String name, String surname, String email, BigDecimal salary, String departmentName, String companyName) {
        String query;
        //noinspection SqlResolve
        query = "SELECT employee_id, department_id, company_id FROM register_employee(\n" +
                "  _name := :name, \n" +
                "  _surname := :surname, \n" +
                "  _email := :email, \n" +
                "  _salary := :salary, \n" +
                "  _department_name := :departmentName, \n" +
                "  _company_name := :companyName\n" +
                ")";


        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", name)
                .addValue("surname", surname)
                .addValue("email", email)
                .addValue("salary", salary)
                .addValue("departmentName", departmentName)
                .addValue("companyName", companyName);

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
    public Integer getProjectsCount() {
        String query = "SELECT count(*) FROM project";
        return jdbcTemplate.getJdbcOperations().queryForObject(query, Integer.class);
    }

    @Override
    public List<Department> findDepartmentsOfCompany(Company company) {
        String query = "SELECT pid, company_pid, name" +
                "           FROM department " +
                "           WHERE company_pid = :pid" +
                "           ORDER BY pid";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("pid", company.getPid());

        return jdbcTemplate.query(query, params, BeanPropertyRowMapper.newInstance(Department.class));
    }

    @Override
    public void deleteDepartments(List<Department> departmentsToDelete) {
        List<Integer> ids = departmentsToDelete.stream()
                .map(Department::getPid)
                .collect(Collectors.toList());

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("pids", ids);

        String updateStatement = "DELETE FROM department WHERE pid IN (:pids)";

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

    @Override
    public Project findProject(Integer pid) {
        String query;
        query = "SELECT pid, name, datestarted AS date" +
                "           FROM project " +
                "           WHERE pid = :pid";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("pid", pid);

        // using BeanPropertyRowMapper is easier, but with much worse performance than custom RowMapper
        return jdbcTemplate.queryForObject(query, params, BeanPropertyRowMapper.newInstance(Project.class));
    }

    private void batchUpdateDepartments(List<Department> departmentsToInsert, String statement) {
        MapSqlParameterSource[] paramsList = departmentsToInsert
                .stream()
                .map(department -> new MapSqlParameterSource()
                        .addValue("pid", department.getPid())
                        .addValue("company_pid", department.getCompanyPid())
                        .addValue("name", department.getName())
                )
                .toArray(MapSqlParameterSource[]::new);

        jdbcTemplate.batchUpdate(statement, paramsList);
    }

}
