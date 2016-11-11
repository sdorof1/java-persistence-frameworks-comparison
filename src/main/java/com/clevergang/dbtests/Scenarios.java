package com.clevergang.dbtests;

import com.clevergang.dbtests.repository.api.data.*;
import com.clevergang.dbtests.repository.api.DataRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

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
        repository.insertProjects(projects);
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
        List<Department> departments = repository.findDepartmentsOfCompany(company);

        logger.info("There are {} departments in {} company", departments.size(), company.getName());
    }

    /**
     * Update one-to-many relation (Departments in Company) at once - add two items, update one items and delete one item.
     * <br/>
     * This scenario covers situation where we have no idea what operations were performed by the user. We only
     * have new list of Departments and we have to efficiently update DB so it exactly reflects new Departments list.
     * <br/>
     * At the same time, we DON'T want to take the tempting path and do it by "removing all the existing departments of the company from
     * database and then inserting new values". This is actually not what the user did nor it's what we should do -> what if the departments
     * which were only updated have some additional relations in the database? -> if we delete them, we will delete also those relations ->
     * always ask yourself if this is something you want (or you want risk).
     */
    public void scenarioEight() {
        Company company = repository.findCompany(1); // Clevergang company

        // this call simulates what typically happens in UI - the user chooses new list of departments for company
        // (adds new ones, updates some other or removes some departments). The new list is transferred from
        // UI to business service a List<Department> with no information about what departments were deleted or which
        // ones were updated - the business service has to determine these changes - which is what rest of the code in this method does
        List<Department> newDepartments = createNewDepartmentsList(company);

        updateDepartments(company, newDepartments);

        // now check the results
        List<Department> departmentsForCompany = repository.findDepartmentsOfCompany(company);
        logger.info("State of database at the end of scenario eight: {}", departmentsForCompany);
    }

    private void updateDepartments(Company company, List<Department> newDepartments) {
        // --------------------------------------------------------------------------------------------------------------------------------------
        // The pattern for one-to-many relation update begins here - but BEWARE, it'll work nicely only for few items in the one to many relation
        // --------------------------------------------------------------------------------------------------------------------------------------

        // first get current departments
        List<Department> currentDepartments = repository.findDepartmentsOfCompany(company);

        // now determine which departments were deleted
        Collection<Integer> newPIDs = CollectionUtils.collect(newDepartments, Department::getPid);
        List<Department> deletedDepartments = currentDepartments.stream()
                .filter(department -> !newPIDs.contains(department.getPid()))
                .collect(toList());

        // ... and which were added or updated
        Map<Boolean, List<Department>> addedOrUpdatedDepartments = newDepartments.stream()
                .filter(department -> !currentDepartments.contains(department))  // filtering out not changed items, assumes that equals and hashCode is properly coded in Department class
                .collect(Collectors.partitioningBy(d -> d.getPid() == null));  // split (partition) by new or updated (pid is either null or not)
        List<Department> addedDepartments = addedOrUpdatedDepartments.get(Boolean.TRUE);
        List<Department> updatedDepartments = addedOrUpdatedDepartments.get(Boolean.FALSE);

        // now perform relevant operations on each list:
        repository.deleteDepartments(deletedDepartments);
        repository.insertDepartments(addedDepartments);
        repository.updateDepartments(updatedDepartments);
    }

    private List<Department> createNewDepartmentsList(Company company) {
        List<Department> departments = repository.findDepartmentsOfCompany(company);

        // delete one item
        departments.removeIf(department -> department.getName().equals("Lazy Department"));

        // add two items (notice they don't have thier own pid yet)
        departments.add(new Department(company.getPid(), "New department 1"));
        departments.add(new Department(company.getPid(), "New department 2"));

        // update one item
        Optional<Department> itDepartment = departments.stream()
                .filter(it -> it.getName().equals("IT Department"))
                .findFirst();

        if (itDepartment.isPresent()) {
            itDepartment.get().setName("IT Department Updated");
        }

        return departments;
    }


    /**
     * Execute complex query
     *
     * In our case we are executing following query:
     * Query: get all projects, where the total cost of the project per month is greater than 70000. In the same resultset
     * get all companies participating on such project along with cost of the project for the company.
     */
    public void scenarioNine() {
        List<ProjectsWithCostsGreaterThanOutput> projectsWithCostsGreaterThan = repository.getProjectsWithCostsGreaterThan(70000);

        logger.info("scenarioNine output: {}", projectsWithCostsGreaterThan);
    }

    /**
     * Execute stored procedure/function and process results
     *
     */
    public void scenarioTen() {
        RegisterEmployeeOutput output = repository.callRegisterEmployee("Bretislav", "Wajtr", "bretislav.wajtr@test.com", new BigDecimal(40000), "MyDepartment", "MyCompany");

        logger.info("scenarioTen output: {}", output);
    }

}




