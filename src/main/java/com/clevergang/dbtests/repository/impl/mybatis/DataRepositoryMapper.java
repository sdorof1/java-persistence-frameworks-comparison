package com.clevergang.dbtests.repository.impl.mybatis;

import com.clevergang.dbtests.repository.api.data.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * MyBatis Mapper interface for queries in DataRepositoryMapper.xml
 *
 * @author Bretislav Wajtr
 */
@Mapper
public interface DataRepositoryMapper {

    Company findCompany(Integer pid);

    Company findCompanyStatic(@Param("pid") Integer pid);

    Department findDepartment(Integer pid);

    List<Employee> employeesWithSalaryGreaterThan(Integer minSalary);

    List<ProjectsWithCostsGreaterThanOutput> getProjectsWithCostsGreaterThan(int totalCostBoundary);

    void insertProject(Project project);

    void updateEmployee(Employee employeeToUpdate);

    List<Department> findDepartmentsOfCompany(Company company);

    void deleteDepartments(@Param("departmentsToDelete") List<Department> departmentsToDelete);

    void insertDepartment(Department department);

    void updateDepartment(Department department);

    RegisterEmployeeOutput callRegisterEmployee(@Param("name") String name, @Param("surname") String surname, @Param("email") String email, @Param("salary") BigDecimal salary, @Param("departmentName") String departmentName, @Param("companyName") String companyName);

    Integer getProjectsCount();

    Employee findEmployee(Integer pid);

    Project findProject(Integer pid);
}
