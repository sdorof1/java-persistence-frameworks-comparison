package com.clevergang.dbtests.service.repository;

import java.util.List;

import com.clevergang.dbtests.model.Employee;

/**
 * @author Bretislav Wajtr <bretislav.wajtr@clevergang.com>
 */
public interface EmployeeRepository {

    List<Employee> employeesWithSalaryGreaterThan(Integer minSalary);
}
