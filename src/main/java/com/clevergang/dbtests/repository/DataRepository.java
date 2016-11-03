package com.clevergang.dbtests.repository;

import com.clevergang.dbtests.model.Company;
import com.clevergang.dbtests.model.Department;
import com.clevergang.dbtests.model.Employee;
import com.clevergang.dbtests.model.Project;

import java.util.List;

/**
 * In "normal" application we would rather split the repository to multiple classes based on entity
 * we want to work with (CompanyRepository, DepartmentRepository etc.). We decided
 * to create just a single interface in this project, so different implementations are more easily compared...
 *
 * @author Bretislav Wajtr <bretislav.wajtr@clevergang.com>
 */
public interface DataRepository {

    // companies

    Company findCompany(Integer pid);

    // departments

    Department findDepartment(Integer pid);

    List<Department> findDepartmentsOfCompany(Company company);

    void deleteDepartments(List<Department> departmentsToDelete);

    void updateDepartments(List<Department> departmentsToUpdate);

    void insertDepartments(List<Department> departmentsToInsert);

    // projects

    Integer insertProject(Project project);

    List<Integer> insertProjects(List<Project> projects);

    // employees

    List<Employee> employeesWithSalaryGreaterThan(Integer minSalary);

    void updateEmployee(Employee employeeToUpdate);

}
