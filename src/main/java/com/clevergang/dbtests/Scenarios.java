package com.clevergang.dbtests;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.clevergang.dbtests.model.Employee;
import com.clevergang.dbtests.model.Project;
import com.clevergang.dbtests.service.repository.DataRepository;
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

    public void scenarioSix() {

    }

    public void scenarioSeven() {

    }

    public void scenarioEight() {

    }
}



