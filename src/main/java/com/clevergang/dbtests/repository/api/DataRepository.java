package com.clevergang.dbtests.repository.api;

import com.clevergang.dbtests.repository.api.data.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * In "normal" application we would rather split the repository to multiple classes based on entity
 * we want to work with (CompanyRepository, DepartmentRepository etc.). We decided
 * to create just a single interface in this project, so different implementations are more easily compared...
 *
 * @author Bretislav Wajtr
 */
public interface DataRepository {

    // Companies

    /**
     * @param pid Primary key of the company record to be found
     * @return Should return full record of the Company record identified by pid
     */
    Company findCompany(Integer pid);

    /**
     * @param pid Primary key of the company record to be found
     * @return Should return full record of the Company record identified by pid. A static (non-prepared) statement should be used for that.
     */
    Company findCompanyUsingSimpleStaticStatement(Integer pid);



    // Departments

    /**
     * @param pid Primary key of the Department record to be found
     * @return Should return full record of the Department record identified by pid
     */
    Department findDepartment(Integer pid);

    /**
     * Should return full records of all Departments assigned to the Company (passed in as parameter).
     * @param company Full Company record
     * @return List of Departments
     */
    List<Department> findDepartmentsOfCompany(Company company);

    /**
     * Should immediately delete Departments passed in as parameter from database. Should do it in most efficient way (preferably as batch operation).
     * We expect that the deletions were already performed in database upon return from this method.
     *
     * @param departmentsToDelete Full records of Departments to be deleted
     */
    void deleteDepartments(List<Department> departmentsToDelete);

    /**
     * Should immediately update Department records passed in as parameter. Should do it in most efficient way (preferably as batch operation).
     * We expect that the updates were already performed in database upon return from this method (meaning all required SQL statements were already
     * executed and are not deferred to a later time).
     *
     * @param departmentsToUpdate Full records of Departments to be updated. Update all fields of Department, identified by Department.getPid()
     */
    void updateDepartments(List<Department> departmentsToUpdate);

    /**
     * Should immediately insert Department records passed in as parameter. Should do it in most efficient way (preferably as batch operation).
     * We expect that the inserts were already performed in database upon return from this method (meaning all required SQL statements were already
     * executed and are not deferred to a later time).
     *
     * @param departmentsToInsert Full records of Departments to be inserted. Records should have getPid()==null
     */
    void insertDepartments(List<Department> departmentsToInsert);




    // Projects

    /**
     * @param pid Primary key of the project record to be found
     * @return Should return full record of the Project record identified by pid
     */
    Project findProject(Integer pid);

    /**
     * Insert project and return PID of newly created item
     *
     * @param project Project to be inserted
     * @return PID of new record in database
     */
    Integer insertProject(Project project);

    /**
     * Should immediately insert Project records passed in as parameter. Should do it in most efficient way (preferably as batch operation).
     * We expect that the inserts were already performed in database upon return from this method (meaning all required SQL statements were already
     * executed and are not deferred to a later time).
     *
     * @param projects New projects to insert
     * @return List of PIDs of newly created records.
     */
    List<Integer> insertProjects(List<Project> projects);

    /**
     * Execute following query: get all projects, where the total cost of the project per month is greater than parameter totalCostBoundary. In the same result set
     * get all companies participating on such project along with cost of the project for the company.
     */
    List<ProjectsWithCostsGreaterThanOutput> getProjectsWithCostsGreaterThan(int totalCostBoundary);



    // Employees

    /**
     * @param pid Primary key of the employee record to be found
     * @return Should return full record of the Employee record identified by pid
     */
    Employee findEmployee(Integer pid);


    /**
     * Execute following query and return all Employee records satisfying following condition: salary of the employee
     * is greater than parameter minSalary
     */
    List<Employee> employeesWithSalaryGreaterThan(Integer minSalary);


    /**
     * Should immediately update Employee record passed in as parameter. Should do it in most efficient way.
     * We expect that the update was already performed in database upon return from this method (meaning SQL statement was already
     * executed and is not deferred to a later time).
     *
     * @param employeeToUpdate Full records of Employee to be updated. Update all fields of Employee, identified by Employee.getPid()
     */
    void updateEmployee(Employee employeeToUpdate);


    /**
     * Register new employee and create company and department if required - use stored procedure register_employee (see register_employee.sql)
     * to perform the operation.
     *
     * @return Newly created PIDs of records (output of the stored procedure)
     */
    RegisterEmployeeOutput callRegisterEmployee(String name, String surname, String email, BigDecimal salary,
                                                String departmentName, String companyName);

    /**
     * @return Return count of records in Project table
     */
    Integer getProjectsCount();
}
