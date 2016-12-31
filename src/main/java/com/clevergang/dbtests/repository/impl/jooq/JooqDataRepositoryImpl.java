package com.clevergang.dbtests.repository.impl.jooq;

import com.clevergang.dbtests.repository.api.DataRepository;
import com.clevergang.dbtests.repository.api.data.*;
import com.clevergang.dbtests.repository.impl.jooq.generated.Routines;
import com.clevergang.dbtests.repository.impl.jooq.generated.routines.RegisterEmployee;
import com.clevergang.dbtests.repository.impl.jooq.generated.tables.records.DepartmentRecord;
import com.clevergang.dbtests.repository.impl.jooq.generated.tables.records.EmployeeRecord;
import org.jooq.*;
import org.jooq.conf.Settings;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static com.clevergang.dbtests.repository.impl.jooq.generated.Tables.*;
import static org.apache.commons.collections4.CollectionUtils.collect;
import static org.jooq.impl.DSL.*;


/**
 * jOOQ implementation of the DataRepository
 *
 * @author Bretislav Wajtr
 */
@Repository
public class JooqDataRepositoryImpl implements DataRepository {
    private static final Logger logger = LoggerFactory.getLogger(JooqDataRepositoryImpl.class);

    private final DSLContext create;
    private final Settings staticStatementSettings;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    public JooqDataRepositoryImpl(DSLContext create, @Qualifier("static-statement-jooq-settings") Settings staticStatementSettings) {
        this.create = create;
        this.staticStatementSettings = staticStatementSettings;
    }

    @Override
    public Company findCompany(Integer pid) {
        logger.info("Finding Company by ID using JOOQ");

        Company company = create.
                selectFrom(COMPANY)
                .where(COMPANY.PID.eq(pid))
                .fetchOneInto(Company.class);

        logger.info("Found company: " + company);
        return company;
    }

    @Override
    public Company findCompanyUsingSimpleStaticStatement(Integer pid) {
        Configuration configuration = create.configuration().derive(staticStatementSettings);
        return DSL.using(configuration)
            .selectFrom(COMPANY)
            .where(COMPANY.PID.eq(pid))
            .fetchOneInto(Company.class);
    }

    @Override
    public void removeProject(Integer pid) {
        create.deleteFrom(PROJECT)
                .where(PROJECT.PID.eq(pid))
                .execute();
    }

    @Override
    public Department findDepartment(Integer pid) {
        return create.
                selectFrom(DEPARTMENT)
                .where(DEPARTMENT.PID.eq(pid))
                .fetchOneInto(Department.class);
    }

    @Override
    public List<Employee> employeesWithSalaryGreaterThan(Integer minSalary) {
        logger.info("Looking for employeesWithSalaryGreaterThan using JOOQ");

        return create
                .select()
                .from(EMPLOYEE)
                .where(EMPLOYEE.SALARY.greaterThan(new BigDecimal(minSalary)))
                .fetchInto(Employee.class);
    }

    @Override
    public Integer insertProject(Project project) {
        logger.info("Inserting project using JOOQ");

        return create
                .insertInto(PROJECT)
                .set(PROJECT.NAME, project.getName())
                .set(PROJECT.DATESTARTED, Date.valueOf(project.getDate()))
                .returning(PROJECT.PID)
                .fetchOne()
                .getPid();
    }

    @Override
    public List<Integer> insertProjects(List<Project> projects) {
        logger.info("Batch inserting projects using JOOQ");

        // FIXME This is weird syntax (especially those null dummy values) but works and showing same speed as JDBCTemplate
        // FIXME But there is another way of executing batch operations -> see method updateDepartments() below
        // prepare batch statement
        BatchBindStep batch = create.batch(
                create
                        .insertInto(PROJECT,
                                PROJECT.NAME,
                                PROJECT.DATESTARTED)
                        .values((String) null, null));

        // bind values
        for (Project project : projects) {
            batch.bind(
                    project.getName(),
                    Date.valueOf(project.getDate())
            );
        }

        // execute batch
        batch.execute();

        // FIXME Not even JOOQ can easily return newly generated ID's from batch operations
        return null;
    }

    @Override
    public void updateEmployee(Employee employeeToUpdate) {
        logger.info("Updating employee using JOOQ");

        // If field names of the DTO object passed into this method match field names in the jooq Record class,
        // then it's easier to use jooq Records when updating all fields of the entity at once (Employee field match EmployeeRecord fields):
        EmployeeRecord record = new EmployeeRecord();
        record.from(employeeToUpdate);  // <-- reflection based mapping, but you can use JPA @Column annotations too!
        create.executeUpdate(record);

        // If the DTO object fields do not match the employee record, then you'll have to use manual mapping:
        create.update(EMPLOYEE)
                .set(EMPLOYEE.DEPARTMENT_PID, employeeToUpdate.getDepartmentPid())
                .set(EMPLOYEE.NAME, employeeToUpdate.getName())
                .set(EMPLOYEE.SURNAME, employeeToUpdate.getSurname())
                .set(EMPLOYEE.EMAIL, employeeToUpdate.getEmail())
                .set(EMPLOYEE.SALARY, employeeToUpdate.getSalary())
                .where(EMPLOYEE.PID.eq(employeeToUpdate.getPid())) // <-- Do not forget to add where condition!
                .execute();
    }

    @Override
    public List<ProjectsWithCostsGreaterThanOutput> getProjectsWithCostsGreaterThan(int totalCostBoundary) {

        // Actually there are two ways how to do complex queries in JOOQ:
        // You can either use JOOQ DSL and construct the query using JOOQ methods and classes, like in this method here:
        //
        //   return getProjectsWithCostsUsingJooqDSL(totalCostBoundary);
        //
        // But I found this style of writing queries quite difficult to do and not much productive. JOOQ is excellent
        // and unbeatable for simple queries, but for complex queries I find it simpler and more productive to go
        // with native query and then let JOOQ only to map the result, like here:
        return getProjectsWithCostsUsingNativeQuery(totalCostBoundary);
    }

    @Override
    public Employee findEmployee(Integer pid) {
        return create.selectFrom(EMPLOYEE).where(EMPLOYEE.PID.eq(pid)).fetchOneInto(Employee.class);
    }

    @Override
    public RegisterEmployeeOutput callRegisterEmployee(String name, String surname, String email, BigDecimal salary, String departmentName, String companyName) {
        // very easy way how to call a stored procedure. Notice that RegisterEmployee class is generated by jooq...
        RegisterEmployeeOutput result = new RegisterEmployeeOutput();

        RegisterEmployee output = Routines.registerEmployee(create.configuration(), name, surname, email, salary, departmentName, companyName);

        result.setCompanyPid(output.getCompanyId());
        result.setDepartmentPid(output.getDepartmentId());
        result.setEmployeePid(output.getEmployeeId());

        return result;
    }

    @Override
    public Integer getProjectsCount() {
        return create.selectCount().from(PROJECT).fetchOneInto(Integer.class);
    }

    private List<ProjectsWithCostsGreaterThanOutput> getProjectsWithCostsUsingNativeQuery(int totalCostBoundary) {
        // FIXME: We should cache loaded scripts in real production code!
        String query = loadSQLScript("jooq/queries/getProjectsWithCostsUsingNativeQuery.sql");

        return create
                .resultQuery(query, totalCostBoundary)
                .fetchInto(ProjectsWithCostsGreaterThanOutput.class);
    }

    private String loadSQLScript(String resource) {
        String content = null;
        try {
            @SuppressWarnings("ConstantConditions")
            URI uri = getClass().getClassLoader().getResource(resource).toURI();
            content = new String(Files.readAllBytes(Paths.get(uri)));
        } catch (IOException | URISyntaxException e) {
            // we can safely ignore these exceptions, if all is coded well, then
            // there should be no problem in production
            logger.error("Problem with SQL script loading", e);
        }
        return content;
    }

    @SuppressWarnings("unused")
    private List<ProjectsWithCostsGreaterThanOutput> getProjectsWithCostsUsingJooqDSL(int totalCostBoundary) {
        CommonTableExpression<Record4<Integer, String, BigDecimal, String>> project_info =
                name("project_info")
                        .fields("project_pid", "project_name", "monthly_cost", "company_name")
                        .as(select(PROJECT.PID, PROJECT.NAME, EMPLOYEE.SALARY, COMPANY.NAME)
                                .from(PROJECT)
                                .join(PROJECTEMPLOYEE).on(PROJECT.PID.eq(PROJECTEMPLOYEE.PROJECT_PID))
                                .join(EMPLOYEE).on(PROJECTEMPLOYEE.EMPLOYEE_PID.eq(EMPLOYEE.PID))
                                .leftJoin(DEPARTMENT).on(EMPLOYEE.DEPARTMENT_PID.eq(DEPARTMENT.PID))
                                .leftJoin(COMPANY).on(DEPARTMENT.COMPANY_PID.eq(COMPANY.PID))
                        );

        Field<BigDecimal> monthly_cost = project_info.field("monthly_cost", BigDecimal.class);
        Field<?> project_name = project_info.field("project_name");
        Field<?> company_name = project_info.field("company_name");

        CommonTableExpression<Record2<Integer, BigDecimal>> project_cost =
                name("project_cost")
                        .fields("project_pid", "total_cost")
                        .as(select(project_info.field("project_pid", Integer.class), sum(monthly_cost))
                                .from(project_info)
                                .groupBy(project_info.field("project_pid"))
                        );

        Field<BigDecimal> total_cost = project_cost.field("total_cost", BigDecimal.class);

        SelectSeekStep1<? extends Record4<?, BigDecimal, ?, BigDecimal>, ?> query = create
                .with(project_info)
                .with(project_cost)
                .select(project_name, total_cost, company_name, sum(monthly_cost).as("company_cost"))
                .from(project_info)
                .join(project_cost).using(project_info.field("project_pid"))
                .where(total_cost.greaterThan(new BigDecimal(totalCostBoundary)))
                .groupBy(project_name, total_cost, company_name)
                .orderBy(company_name);


        return query.fetchInto(ProjectsWithCostsGreaterThanOutput.class);
    }

    @Override
    public List<Department> findDepartmentsOfCompany(Company company) {
        return create.
                selectFrom(DEPARTMENT)
                .where(DEPARTMENT.COMPANY_PID.eq(company.getPid()))
                .orderBy(DEPARTMENT.PID)
                .fetchInto(Department.class);
    }

    @Override
    public void deleteDepartments(List<Department> departmentsToDelete) {
        Collection<Integer> pids = collect(departmentsToDelete, Department::getPid);
        create.deleteFrom(DEPARTMENT)
                .where(DEPARTMENT.PID.in(pids))
                .execute();
    }

    @Override
    public void updateDepartments(List<Department> departmentsToUpdate) {
        // one way of executing batch update is through create.batchUpdate (other way is shown in insertDepartments() method below),
        // for which we need list of UpdatableRecords, this stream operation creates it using record.from operation, which relies on reflection mapping
        List<DepartmentRecord> records = departmentsToUpdate.stream()
                .map(department -> {
                    DepartmentRecord record = new DepartmentRecord();
                    record.from(department);  // <-- reflection based mapping, but you can use JPA @Column annotations too!
                    return record;
                }).collect(Collectors.toList());

        // execute batch
        create.batchUpdate(records).execute();
    }

    @Override
    public void insertDepartments(List<Department> departmentsToInsert) {
        // FIXME This is weird syntax (especially those null dummy values) but works and showing same speed as JDBCTemplate
        // FIXME But there is another way of executing batch operations -> see method updateDepartments() above
        // prepare batch statement
        BatchBindStep batch = create.batch(
                create
                        .insertInto(DEPARTMENT,
                                DEPARTMENT.NAME,
                                DEPARTMENT.COMPANY_PID)
                        .values((String) null, null));

        // bind values
        for (Department department : departmentsToInsert) {
            batch.bind(
                    department.getName(),
                    department.getCompanyPid()
            );
        }

        // execute batch
        batch.execute();
    }

    @Override
    public Project findProject(Integer pid) {
        return create.selectFrom(PROJECT)
                .where(PROJECT.PID.eq(pid))
                .fetchOne(project -> {
                    Project result = new Project();
                    result.setPid(project.getPid());
                    result.setName(project.getName());
                    result.setDate(project.getDatestarted().toLocalDate());
                    return result;
                });
    }

}

