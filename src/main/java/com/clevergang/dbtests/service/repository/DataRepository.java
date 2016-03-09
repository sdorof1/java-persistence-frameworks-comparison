package com.clevergang.dbtests.service.repository;

import java.util.List;

import com.clevergang.dbtests.model.Company;
import com.clevergang.dbtests.model.Employee;
import com.clevergang.dbtests.model.Project;

/**
 * @author Bretislav Wajtr <bretislav.wajtr@clevergang.com>
 */
public interface DataRepository {

    Company findCompany(Integer pid);

    List<Employee> employeesWithSalaryGreaterThan(Integer minSalary);

    Integer insertProject(Project project);

    List<Integer> insertAllProjects(List<Project> projects);

}
