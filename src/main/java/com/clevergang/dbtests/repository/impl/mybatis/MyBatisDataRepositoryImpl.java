package com.clevergang.dbtests.repository.impl.mybatis;

import com.clevergang.dbtests.repository.api.DataRepository;
import com.clevergang.dbtests.repository.api.data.Company;
import com.clevergang.dbtests.repository.api.data.Department;
import com.clevergang.dbtests.repository.api.data.Employee;
import com.clevergang.dbtests.repository.api.data.ProjectsWithCostsGreaterThanOutput;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * "Implementation" of the DataRepository using MyBatis. Note, that in this project
 * we have to extend the DataRepository interface, because this interface is actually
 * implemented also by other technologies - however if we decide for MyBatis as a
 * SQL mapping framework for some other project, it would be an overkill to do it this way. We would
 * directly use this @Mapper interface as a Spring bean.
 *
 * @author Bretislav Wajtr <bretislav.wajtr@ibacz.eu>
 */
@Mapper
public interface MyBatisDataRepositoryImpl extends DataRepository {

    Company findCompany(Integer pid);

    Department findDepartment(Integer pid);

    List<Employee> employeesWithSalaryGreaterThan(Integer minSalary);

    List<ProjectsWithCostsGreaterThanOutput> getProjectsWithCostsGreaterThan(int totalCostBoundary);
}
