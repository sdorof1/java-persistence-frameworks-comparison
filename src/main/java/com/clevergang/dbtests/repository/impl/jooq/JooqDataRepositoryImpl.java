package com.clevergang.dbtests.repository.impl.jooq;

import com.clevergang.dbtests.model.Company;
import com.clevergang.dbtests.model.Department;
import com.clevergang.dbtests.model.Employee;
import com.clevergang.dbtests.model.Project;
import com.clevergang.dbtests.repository.DataRepository;
import com.clevergang.dbtests.repository.impl.jooq.generated.tables.records.DepartmentRecord;
import com.clevergang.dbtests.repository.impl.jooq.generated.tables.records.EmployeeRecord;
import org.jooq.BatchBindStep;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static com.clevergang.dbtests.repository.impl.jooq.generated.Tables.*;
import static org.apache.commons.collections4.CollectionUtils.collect;


/**
 * @author Bretislav Wajtr <bretislav.wajtr@clevergang.com>
 */
@Repository
public class JooqDataRepositoryImpl implements DataRepository {
    private static final Logger logger = LoggerFactory.getLogger(JooqDataRepositoryImpl.class);

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    private DSLContext create;

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

}

