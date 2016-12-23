package com.clevergang.dbtests.repository.impl.mybatis;

import com.clevergang.dbtests.repository.api.DataRepository;
import com.clevergang.dbtests.repository.api.data.*;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of the DataRepository using MyBatis. One might wonder, why I broke the usual MyBatis usage pattern to directly use @Mapper
 * interfaces in the Business Layer and why most of the methods are just delegating to DataRepositoryMapper. I do not see that "usual" pattern
 * as a great idea:
 * You cannot make any modification to how the SQL is executed, you cannot do any pre or post actions (not even simple logging), etc.
 * Therefore, I believe that better design choice is to wrap the @Mapper with custom class (even if it's only Delegator) - on one
 * side you loose some of the beauty, on the other side future extensions/modifications are much easier (not to speak about mocking).
 * In our case it's necessary anyway because at the end of batch operations (like methods updateDepartments and insertDepartments) we
 * expect that the records are already inserted/updated -> you have to call flushStatements() in MyBatis to achieve that.
 *
 * @author Bretislav Wajtr
 */
@Repository
public class MyBatisDataRepositoryImpl implements DataRepository {

    private final DataRepositoryMapper sql;
    private final SqlSession batchOperations;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    public MyBatisDataRepositoryImpl(DataRepositoryMapper sql, @Qualifier("batch-operations") SqlSession batchOperations) {
        this.sql = sql;
        this.batchOperations = batchOperations;
    }

    @Override
    public Company findCompany(Integer pid) {
        return sql.findCompany(pid);
    }

    @Override
    public Company findCompanyUsingSimpleStaticStatement(Integer pid) {
        /*
         * !!!!! NOTE THAT you can actually use parameters for SQL with statementType=STATEMENT, but you have to use ${pid} notation
         * (notice $ instead of #) in the mapping xml!
         */
        return sql.findCompanyStatic(pid);
    }

    @Override
    public void removeProject(Integer pid) {
        sql.removeProject(pid);
    }

    @Override
    public Department findDepartment(Integer pid) {
        return sql.findDepartment(pid);
    }

    @Override
    public List<Department> findDepartmentsOfCompany(Company company) {
        return sql.findDepartmentsOfCompany(company);
    }

    @Override
    public void deleteDepartments(List<Department> departmentsToDelete) {
        sql.deleteDepartments(departmentsToDelete);
    }

    @Override
    public void updateDepartments(List<Department> departmentsToUpdate) {
        DataRepositoryMapper batchSql = batchOperations.getMapper(DataRepositoryMapper.class);
        departmentsToUpdate.forEach(batchSql::updateDepartment);
        // we have to flush statements here - the records would be updated only at the end of the transaction otherwise
        batchOperations.flushStatements();
    }

    @Override
    public void insertDepartments(List<Department> departmentsToInsert) {
        DataRepositoryMapper batchSql = batchOperations.getMapper(DataRepositoryMapper.class);
        departmentsToInsert.forEach(batchSql::insertDepartment);
        // we have to flush statements here - the records would be inserted only at the end of the transaction otherwise
        batchOperations.flushStatements();
    }

    @Override
    public Project findProject(Integer pid) {
        return sql.findProject(pid);
    }

    @Override
    public Integer insertProject(Project project) {
        sql.insertProject(project);
        return project.getPid();
    }

    @Override
    public List<Integer> insertProjects(List<Project> projects) {
        DataRepositoryMapper batchSql = batchOperations.getMapper(DataRepositoryMapper.class);
        projects.forEach(batchSql::insertProject);
        // we have to flush statements here - the records would be inserted only at the end of the transaction otherwise
        batchOperations.flushStatements();

        // MyBatis can actually return the PIDs from batch operation
        return projects.stream().map(Project::getPid).collect(Collectors.toList());
    }

    @Override
    public List<ProjectsWithCostsGreaterThanOutput> getProjectsWithCostsGreaterThan(int totalCostBoundary) {
        return sql.getProjectsWithCostsGreaterThan(totalCostBoundary);
    }

    @Override
    public Employee findEmployee(Integer pid) {
        return sql.findEmployee(pid);
    }

    @Override
    public List<Employee> employeesWithSalaryGreaterThan(Integer minSalary) {
        return sql.employeesWithSalaryGreaterThan(minSalary);
    }

    @Override
    public void updateEmployee(Employee employeeToUpdate) {
        sql.updateEmployee(employeeToUpdate);
    }

    @Override
    public RegisterEmployeeOutput callRegisterEmployee(String name, String surname, String email, BigDecimal salary, String departmentName, String companyName) {
        return sql.callRegisterEmployee(name, surname, email, salary, departmentName, companyName);
    }

    @Override
    public Integer getProjectsCount() {
        return sql.getProjectsCount();
    }
}
