package com.clevergang.dbtests.repository.impl.ebean;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.SqlRow;
import com.clevergang.dbtests.repository.api.DataRepository;
import com.clevergang.dbtests.repository.api.data.*;
import com.clevergang.dbtests.repository.impl.ebean.entities.CompanyEntity;
import com.clevergang.dbtests.repository.impl.ebean.entities.DepartmentEntity;
import com.clevergang.dbtests.repository.impl.ebean.entities.EmployeeEntity;
import com.clevergang.dbtests.repository.impl.ebean.entities.ProjectEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.stream.Collectors;

/**
 * EBean implementation of DataRepository interface...
 *
 * @author Bretislav Wajtr
 */
@Repository
public class EBeanDataRepositoryImpl implements DataRepository {

    private final EbeanServer ebean;

    @Autowired
    public EBeanDataRepositoryImpl(EbeanServer ebean) {
        this.ebean = ebean;
    }

    @Override
    public Company findCompany(Integer pid) {
        CompanyEntity companyEntity = ebean.find(CompanyEntity.class, pid);

        Company result = new Company();
        result.setPid(companyEntity.getPid());
        result.setName(companyEntity.getName());
        result.setAddress(companyEntity.getAddress());
        return result;
    }

    @Override
    public Company findCompanyUsingSimpleStaticStatement(Integer pid) {
        // I didn't find a way how to setup EBean so the query is executed statically (using JDBC regular Statement
        // not PreparedStatement). However, EBean is polite and provides way how to access the underlying JDBC connection
        // -> therefore we can actually fall back to JDBC way of doing things (which is not such a big deal since we need simple
        // Statements only rarely):
        try {
            String query = "SELECT pid, address, name " +
                    "FROM company " +
                    "WHERE pid = " + pid;

            Connection connection = ebean.currentTransaction().getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            Company result = null;
            if (resultSet.next()) {
                result = new Company();
                result.setPid(resultSet.getInt("pid"));
                result.setAddress(resultSet.getString("address"));
                result.setName(resultSet.getString("name"));

            }
            return result;
        } catch (SQLException e) {
            // just wrap checked exceptions as e
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeProject(Integer pid) {
        ebean.delete(ProjectEntity.class, pid);
    }

    @Override
    public Department findDepartment(Integer pid) {
        DepartmentEntity departmentEntity = ebean.find(DepartmentEntity.class, pid);

        Department result = new Department();
        result.setPid(departmentEntity.getPid());
        result.setName(departmentEntity.getName());
        result.setCompanyPid(departmentEntity.getCompanyPid());
        return result;
    }

    @Override
    public List<Department> findDepartmentsOfCompany(Company company) {
        List<DepartmentEntity> departments = ebean
                .find(DepartmentEntity.class)
                .where()
                .eq("companyPid", company.getPid())
                .orderBy("pid")
                .findList();

        return departments.stream()
                .map(entity -> {
                    Department department = new Department();
                    department.setPid(entity.getPid());
                    department.setName(entity.getName());
                    department.setCompanyPid(entity.getCompanyPid());
                    return department;
                }).collect(Collectors.toList());
    }

    @Override
    public void deleteDepartments(List<Department> departmentsToDelete) {
        List<Integer> ids = departmentsToDelete.stream().map(Department::getPid).collect(Collectors.toList());
        ebean.deleteAll(DepartmentEntity.class, ids);
    }

    @Override
    public void updateDepartments(List<Department> departmentsToUpdate) {
        List<DepartmentEntity> entities = departmentsToUpdate.stream()
                .map(department -> {
                    DepartmentEntity entity = new DepartmentEntity();
                    entity.setPid(department.getPid());
                    entity.setName(department.getName());
                    entity.setCompanyPid(department.getCompanyPid());
                    return entity;
                }).collect(Collectors.toList());

        ebean.updateAll(entities);
    }

    @Override
    public void insertDepartments(List<Department> departmentsToInsert) {
        List<DepartmentEntity> entities = departmentsToInsert.stream()
                .map(department -> {
                    DepartmentEntity entity = new DepartmentEntity();
                    entity.setName(department.getName());
                    entity.setCompanyPid(department.getCompanyPid());
                    return entity;
                }).collect(Collectors.toList());

        // Please see comment about batching in insertProjects() method
        ebean.saveAll(entities);
    }

    @Override
    public Project findProject(Integer pid) {
        ProjectEntity projectEntity = ebean.find(ProjectEntity.class, pid);

        Project result = new Project();
        result.setPid(projectEntity.getPid());
        result.setName(projectEntity.getName());
        result.setDate(projectEntity.getDateStarted());
        return result;
    }

    @Override
    public Integer insertProject(Project project) {
        ProjectEntity entity = new ProjectEntity();

        entity.setName(project.getName());
        entity.setDateStarted(project.getDate());

        ebean.save(entity);
        return entity.getPid();
    }

    @Override
    public List<Integer> insertProjects(List<Project> projects) {
        List<ProjectEntity> entities = projects.stream()
                .map(project -> {
                    ProjectEntity entity = new ProjectEntity();
                    entity.setName(project.getName());
                    entity.setDateStarted(project.getDate());
                    return entity;
                }).collect(Collectors.toList());

        // Actually EBean handles batch inserts/updates in a very clever way. The default setting for batching is
        // following:
        //    ebean.currentTransaction().getBatch() == PersistBatch.NONE
        //    ebean.currentTransaction().getBatchOnCascade() ==  PersistBatch.ALL
        // so basically when you do single insert (ebean.save()) then batching is not used. But when
        // use use ebean.saveAll, then value for BatchOnCascade is used for storing the value and in
        // our case that's PersistBatch.ALL -> batching is used.
        // That means, for default settings:
        //   ebean.save()  --> no batching is used
        //   ebean.saveAll()  --> batching is automatically used
        // WHICH IS AWESOME!
        ebean.saveAll(entities);

        return entities.stream().map(ProjectEntity::getPid).collect(Collectors.toList());
    }

    @Override
    public List<ProjectsWithCostsGreaterThanOutput> getProjectsWithCostsGreaterThan(int totalCostBoundary) {
        String query;
        query = "WITH project_info AS (\n" +
                "    SELECT project.pid project_pid, project.name project_name, salary monthly_cost, company.name company_name\n" +
                "    FROM project\n" +
                "      JOIN projectemployee ON project.pid = projectemployee.project_pid\n" +
                "      JOIN employee ON projectemployee.employee_pid = employee.pid\n" +
                "      LEFT JOIN department ON employee.department_pid = department.pid\n" +
                "      LEFT JOIN company ON department.company_pid = company.pid\n" +
                "),\n" +
                "project_cost AS (\n" +
                "    SELECT project_pid, sum(monthly_cost) total_cost\n" +
                "    FROM project_info GROUP BY project_pid\n" +
                ")\n" +
                "SELECT project_name, total_cost, company_name, sum(monthly_cost) company_cost FROM project_info\n" +
                "  JOIN project_cost USING (project_pid)\n" +
                "WHERE total_cost > :totalCostBoundary\n" +
                "GROUP BY project_name, total_cost, company_name\n" +
                "ORDER BY company_name";

        return ebean.createSqlQuery(query)
                .setParameter("totalCostBoundary", totalCostBoundary)
                .findList()
                .stream()
                .map(row -> {
                    ProjectsWithCostsGreaterThanOutput output = new ProjectsWithCostsGreaterThanOutput();
                    output.setProjectName(row.getString("project_name"));
                    output.setTotalCost(row.getBigDecimal("total_cost"));
                    output.setCompanyName(row.getString("company_name"));
                    output.setCompanyCost(row.getBigDecimal("company_cost"));
                    return output;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Employee findEmployee(Integer pid) {
        EmployeeEntity entity = ebean.find(EmployeeEntity.class, pid);

        Employee result = new Employee();
        result.setPid(entity.getPid());
        result.setName(entity.getName());
        result.setDepartmentPid(entity.getDepartmentPid());
        result.setEmail(entity.getEmail());
        result.setSalary(entity.getSalary());
        result.setSurname(entity.getSurname());

        return result;
    }

    @Override
    public List<Employee> employeesWithSalaryGreaterThan(Integer minSalary) {
        return ebean
                .find(EmployeeEntity.class)
                .where()
                .gt("salary", minSalary)
                .findList() // query executed here
                .stream()
                .map(entity -> {
                    Employee emp = new Employee();
                    emp.setPid(entity.getPid());
                    emp.setSurname(entity.getSurname());
                    emp.setName(entity.getName());
                    emp.setSalary(entity.getSalary());
                    emp.setEmail(entity.getEmail());
                    emp.setDepartmentPid(entity.getDepartmentPid());
                    return emp;
                }).collect(Collectors.toList());
    }

    @Override
    public void updateEmployee(Employee employeeToUpdate) {
        EmployeeEntity entity = new EmployeeEntity();
        entity.setPid(employeeToUpdate.getPid());
        entity.setEmail(employeeToUpdate.getEmail());
        entity.setName(employeeToUpdate.getName());
        entity.setSurname(employeeToUpdate.getSurname());
        entity.setSalary(employeeToUpdate.getSalary());
        entity.setDepartmentPid(employeeToUpdate.getDepartmentPid());

        ebean.update(entity);
    }

    @Override
    public RegisterEmployeeOutput callRegisterEmployee(String name, String surname, String email, BigDecimal salary, String departmentName, String companyName) {
        String query = "SELECT employee_id, department_id, company_id FROM register_employee(:name, :surname, :email, :salary, :departmentName, :companyName)";

        SqlRow row = ebean.createSqlQuery(query)
                .setParameter("name", name)
                .setParameter("surname", surname)
                .setParameter("email", email)
                .setParameter("salary", salary)
                .setParameter("departmentName", departmentName)
                .setParameter("companyName", companyName)
                .findUnique();

        RegisterEmployeeOutput result = new RegisterEmployeeOutput();
        result.setEmployeePid(row.getInteger("employee_id"));
        result.setDepartmentPid(row.getInteger("department_id"));
        result.setCompanyPid(row.getInteger("company_id"));

        // this is required so the EBean caches are invalidated
        Ebean.externalModification("department", true, false, false);
        Ebean.externalModification("company", true, false, false);
        Ebean.externalModification("employee", true, false, false);

        return result;
    }

    @Override
    public Integer getProjectsCount() {
        return ebean.find(ProjectEntity.class).findCount();
    }
}
