package com.clevergang.dbtests;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.clevergang.dbtests.model.Company;
import com.clevergang.dbtests.model.Department;
import com.clevergang.dbtests.model.Employee;
import com.clevergang.dbtests.model.Project;
import com.clevergang.dbtests.repository.DataRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;

/**
 * Common scenarios
 *
 * @author Bretislav Wajtr <bretislav.wajtr@clevergang.com>
 */
public class Scenarios {
    private static final Logger logger = LoggerFactory.getLogger(Scenarios.class);

    private DataRepository repository;

    public Scenarios(DataRepository repository) {
        this.repository = repository;
    }

    /**
     * Load single entity based on primary key
     */
    public void scenarioOne() {
        Integer pid = 1;
        repository.findCompany(pid);
    }

    /**
     * Load list of entities based on condition
     */
    public void scenarioTwo() {
        Integer minSalary = 2000;
        repository.employeesWithSalaryGreaterThan(minSalary);
    }

    /**
     * Save new single entity and return primary key
     */
    public void scenarioThree() {
        Project project = new Project();
        project.setName("TestProject");
        project.setDate(LocalDate.now());

        repository.insertProject(project);
    }

    /**
     * Batch insertProject multiple entities of same type and return generated keys
     */
    public void scenarioFour() {
        // create a list of thousand products
        List<Project> projects = new ArrayList<>();
        StopWatch watch = new StopWatch();
        watch.start();
        for (int i = 0; i < 1000; i++) {
            Project project = new Project();
            project.setName(RandomStringUtils.randomAlphabetic(10));
            project.setDate(LocalDate.now());
            projects.add(project);
        }
        repository.insertAllProjects(projects);
        watch.stop();
        logger.info("Total time {} ms", watch.getTotalTimeMillis());
    }

    /**
     * Update single existing entity - update all fields of entity at once
     */
    public void scenarioFive() {
        // Complete already store entity with all fields set - imagine that this object comes from UI edit dialog,
        // which is typical scenario
        Employee employeeToUpdate = new Employee();
        employeeToUpdate.setPid(1);
        employeeToUpdate.setDepartmentPid(1);
        employeeToUpdate.setName("Curt");
        employeeToUpdate.setSurname("Odegaard");
        employeeToUpdate.setEmail("curt.odegaard@updated.com");  // <-- this is updated value
        employeeToUpdate.setSalary(new BigDecimal("15000")); // <-- this is updated value

        // now update the employee in DB
        repository.updateEmployee(employeeToUpdate);

    }

    /**
     * Fetch many-to-one relation (Company for Department)
     */
    public void scenarioSix() {
        Department softwareDevelopmentDepartment = repository.findDepartment(3);

        // Getting Company for Department (many-to-one relation) in JPA is quite easy. You typically have
        // @ManyToOne relation defined in the Department entity class, so once you have instance of Department,
        // you just call department.getCompany() and JPA does the magic for you (typically one or more lazy selects
        // are executed). We don't have any such magical call here, but non-JPA approach is quite straightforward
        // too: we have company_pid, so just ask DataRepository for the record:
        Company company = repository.findCompany(softwareDevelopmentDepartment.getCompanyPid());

        logger.info("Department {} is in the {} company", softwareDevelopmentDepartment.getName(), company.getName());
    }

    /**
     * Fetch one-to-many relation (Departments for Company)
     */
    public void scenarioSeven() {
        Company company = repository.findCompany(1);

        // For one-to-many relations the situation is quite similar to many-to-one relations (scenario six). In JPA this
        // is "easy" - you define @OneToMany relation in the Company entity and then you just call getDepartments() method ->
        // a lazy select is issued and Departments are fetched from DB. However you can also use EAGER FetchType strategy which
        // causes the relation to load along with the primary entity - this behavior is (by our opinion) the source of all evil
        // in JPA... So, in non-JPA approach, we don't have any "eager" loads, just explicit calls for data:
        List<Department> departments = repository.getDepartmentsForCompany(company.getPid());

        logger.info("There are {} departments in {} company", departments.size(), company.getName());
    }

    public void scenarioEight() {

    }
}



