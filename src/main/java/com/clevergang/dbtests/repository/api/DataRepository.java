package com.clevergang.dbtests.repository.api;

import com.clevergang.dbtests.repository.api.data.*;

import java.math.BigDecimal;
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

    List<ProjectsWithCostsGreaterThanOutput> getProjectsWithCostsGreaterThan(int totalCostBoundary);

    // employees

    List<Employee> employeesWithSalaryGreaterThan(Integer minSalary);

    void updateEmployee(Employee employeeToUpdate);

    RegisterEmployeeOutput callRegisterEmployee(String name, String surname, String email, BigDecimal salary,
                                                String departmentName, String companyName);
}
