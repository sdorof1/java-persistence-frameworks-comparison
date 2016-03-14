package com.clevergang.dbtests.repository;

import java.util.List;

import com.clevergang.dbtests.model.Company;
import com.clevergang.dbtests.model.Department;
import com.clevergang.dbtests.model.Employee;
import com.clevergang.dbtests.model.Project;

/**
 * In "normal" application we would rather split the repository to multiple classes based on entity
 * we want to work with (CompanyRepository, DepartmentRepository etc.). We decided
 * to create just a single interface in this project, so different implementations are more easily compared...
 *
 * @author Bretislav Wajtr <bretislav.wajtr@clevergang.com>
 */
public interface DataRepository {

    Company findCompany(Integer pid);

    Department findDepartment(Integer pid);

    List<Employee> employeesWithSalaryGreaterThan(Integer minSalary);

    Integer insertProject(Project project);

    List<Integer> insertAllProjects(List<Project> projects);

    void updateEmployee(Employee employeeToUpdate);

    List<Department> getDepartmentsForCompany(Integer pid);
}
