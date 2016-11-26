package com.clevergang.dbtests;

import com.clevergang.dbtests.repository.api.DataRepository;
import com.clevergang.dbtests.repository.api.data.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * Implementation of the scenarios. Note that the scenarios are always the same, what changes is the
 * DB API implementation. To make things little bit easier for us we do not autowire the
 * DB API implementation, but we pass it to the constructor of the Scenarios class instead - this
 * isn't typical pattern we use in production code.
 *
 * @author Bretislav Wajtr <bretislav.wajtr@clevergang.com>
 */
@SuppressWarnings("WeakerAccess")
public class Scenarios {
    private static final Logger logger = LoggerFactory.getLogger(Scenarios.class);

    private final DataRepository repository;

    public Scenarios(DataRepository repository) {
        this.repository = repository;
    }

    /**
     * 1. Fetch single entity based on primary key
     * <br/>
     * This is the case when pid comes from outside (typically from UI) and we need to fetch complete record from database.
     *
     * @param companyPid Primary key of the record coming from outside
     */
    public void fetchSingleEntityScenario(Integer companyPid) {
        Company company = repository.findCompany(companyPid);

        // check some post conditions
        assert company != null;
        assert company.getPid().equals(companyPid);
        assert company.getName().equals("CleverGang");
        logger.info("Fetched result: {}", company);
    }

    /**
     * 2. Fetch list of entities based on condition
     * <br/>
     * This is a case when we want to get records from database using some kind of filter. Filter values typically come from UI.
     * @param employeeMinSalary Example of external filter value
     */
    public void fetchListOfEntitiesScenario(Integer employeeMinSalary) {
        List<Employee> employees = repository.employeesWithSalaryGreaterThan(employeeMinSalary);

        // check some post conditions
        assert employees != null;
        assert employees.size() == 3;
        logger.info("Fetched result: {}", employees);
    }

    /**
     * 3. Save new single entity and return primary key
     */
    public void saveNewEntityScenario() {
        Project project = new Project();
        project.setName("TestProject");
        project.setDate(LocalDate.now());

        // SCENARIO CODE STARTS HERE
        Integer newPid = repository.insertProject(project);

        // check some post conditions
        logger.info("Scenario three, pid of inserted entity: {}", newPid);
        assert newPid != null;
        assert newPid > 2;

        Project storedProject = repository.findProject(newPid);
        logger.info("Scenario three, stored project: {}", storedProject);

        assert storedProject != null;
        assert newPid.equals(storedProject.getPid());
        assert project.getName().equals(storedProject.getName());
        assert project.getDate().equals(storedProject.getDate());
   }

    /**
     * 4. Batch insert multiple entities of same type and return generated keys
     * <br/>
     * This scenario represents a situation, when business method, as a result of it's execution, wants to store
     * multiple records of same type into database effectively.
     * </br>
     * In our scenario method, we create 1000 products first and then we want to store them into the database
     * as fast as we can - through batch insert functionality.
     */
    public void batchInsertMultipleEntitiesScenario() {
        // create a list of thousand products
        List<Project> projects = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            Project project = new Project();
            project.setName(RandomStringUtils.randomAlphabetic(10));
            project.setDate(LocalDate.now());
            projects.add(project);
        }

        // SCENARIO CODE STARTS HERE
        List<Integer> newPids = repository.insertProjects(projects);

        // check some post conditions
        Integer projectsCount = repository.getProjectsCount();
        assert projectsCount == 1002;
        logger.info("Scenario 4. output {}", newPids);
    }

    /**
     * 5. Update single existing entity - update all fields of entity at once
     * <br/>
     * This scenario covers typical situation in information systems where a detail of a record is displayed in UI, user
     * has possibility to modify any field of the record and then he/she presses Save button -> complete record data are sent
     * back to server and the record should be updated in database.
     */
    public void updateCompleteEntityScenario() {
        // Imagine that this object comes from UI edit dialog, which is typical scenario
        Employee employeeToUpdate = performSomeEmployeeRecordModificationsInUI(1);

        // SCENARIO CODE STARTS HERE
        repository.updateEmployee(employeeToUpdate);

        // check some post conditions
        Employee updatedEmployee = repository.findEmployee(1);
        assert employeeToUpdate.getPid().equals(updatedEmployee.getPid());
        assert employeeToUpdate.getDepartmentPid().equals(updatedEmployee.getDepartmentPid());
        assert employeeToUpdate.getName().equals(updatedEmployee.getName());
        assert employeeToUpdate.getSurname().equals(updatedEmployee.getSurname());
        assert employeeToUpdate.getEmail().equals(updatedEmployee.getEmail());
        assert employeeToUpdate.getSalary().equals(updatedEmployee.getSalary());
    }

    private Employee performSomeEmployeeRecordModificationsInUI(Integer employeePid) {
        Employee employeeToUpdate = new Employee();
        employeeToUpdate.setPid(employeePid);
        employeeToUpdate.setDepartmentPid(6);
        employeeToUpdate.setName("Curt1");
        employeeToUpdate.setSurname("Odegaard1");
        employeeToUpdate.setEmail("curt.odegaard@updated.com1");  // <-- this is updated value
        employeeToUpdate.setSalary(new BigDecimal("15000.00")); // <-- this is updated value
        return employeeToUpdate;
    }

    /**
     * 6. Fetch many-to-one relation (Company for Department)
     */
    public void fetchManyToOneRelationScenario() {
        Department softwareDevelopmentDepartment = repository.findDepartment(3);

        // SCENARIO CODE STARTS HERE
        // Getting Company for Department (many-to-one relation) in JPA is quite easy. You typically have
        // @ManyToOne relation defined in the Department entity class, so once you have instance of Department,
        // you just call department.getCompany() and JPA does the magic for you (typically one or more lazy selects
        // are executed). We don't have any such magical call here, but non-JPA approach is quite straightforward
        // too: we have company_pid, so just ask DataRepository for the record:
        Company company = repository.findCompany(softwareDevelopmentDepartment.getCompanyPid());

        // check some post conditions
        assert company.getName().equals("CleverGang");
        assert company.getPid().equals(1);
        logger.info("Department {} is in the {} company", softwareDevelopmentDepartment.getName(), company.getName());
    }

    /**
     * 7. Fetch one-to-many relation (Departments for Company)
     */
    public void fetchOneToManyRelationScenario() {
        Company company = repository.findCompany(1);

        // SCENARIO CODE STARTS HERE
        // For one-to-many relations the situation is quite similar to many-to-one relations (scenario six). In JPA this
        // is "easy" - you define @OneToMany relation in the Company entity and then you just call getDepartments() method ->
        // a lazy select is issued and Departments are fetched from DB. However you can also use EAGER FetchType strategy which
        // causes the relation to load along with the primary entity - this behavior is (by our opinion) the source of all evil
        // in JPA... So, in non-JPA approach, we don't have any "eager" loads, just explicit calls for data:
        List<Department> departments = repository.findDepartmentsOfCompany(company);

        // check some post conditions
        assert departments.size() == 4;
        logger.info("There are {} departments in {} company", departments.size(), company.getName());
    }

    /**
     * 8. Update entities one-to-many relation (Departments in Company) - add two items, update two items and delete one item - all at once
     * <br/>
     * This scenario covers situation where we have no idea what operations were performed by the user. We only
     * have new list of Departments and we have to efficiently update DB so it exactly reflects new Departments list.
     * <br/>
     * At the same time, we DON'T want to take the tempting path and do it by "removing all the existing departments of the company from
     * database and then inserting new values". This is actually not what the user did nor it's what we should do -> what if the departments
     * which were only updated have some additional relations in the database? -> if we delete them, we will delete also those relations ->
     * always ask yourself if this is something you want (or you want risk).
     */
    public void updateCompleteOneToManyRelationScenario() {
        Company company = repository.findCompany(1); // Clevergang company

        // this call simulates what typically happens in UI - the user chooses new list of departments for company
        // (adds new ones, updates some other or removes some departments). The new list is transferred from
        // UI to business service a List<Department> with no information about what departments were deleted or which
        // ones were updated - the business service has to determine these changes - which is what rest of the code in this method does
        List<Department> newDepartments = createNewDepartmentsList(company);


        // SCENARIO CODE STARTS HERE - update departments in DB
        updateDepartments(company, newDepartments);

        // check some post conditions
        List<Department> departmentsForCompany = repository.findDepartmentsOfCompany(company);
        assert departmentsForCompany.size() == 5;
        assert departmentsForCompany.get(0).getName().equals("Back office");
        assert departmentsForCompany.get(1).getName().equals("IT Department Updated");
        assert departmentsForCompany.get(2).getName().equals("Software Development");
        assert departmentsForCompany.get(3).getName().equals("New department 1");
        assert departmentsForCompany.get(4).getName().equals("New department 2");
        logger.info("State of database at the end of scenario eight: {}", departmentsForCompany);
    }

    private void updateDepartments(Company company, List<Department> newDepartments) {
        // --------------------------------------------------------------------------------------------------------------------------------------
        // The pattern for one-to-many relation update begins here - but BEWARE, it'll work nicely only for few items in the one to many relation
        // --------------------------------------------------------------------------------------------------------------------------------------

        // first get current departments
        List<Department> currentDepartments = repository.findDepartmentsOfCompany(company);
        logger.info("Company {} current departments {}", company.getName(), currentDepartments);

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

        // add two items (notice they don't have their own pid yet)
        departments.add(new Department(company.getPid(), "New department 1"));
        departments.add(new Department(company.getPid(), "New department 2"));

        // update one item
        departments.stream()
                .filter(it -> it.getName().equals("IT Department"))
                .findFirst()
                .ifPresent(it -> it.setName("IT Department Updated"));

        return departments;
    }


    /**
     * 9. Complex select - construct select where conditions based on some boolean conditions + throw in some joins
     * <br/>
     * In our case we are executing following query:<br/>
     * Query: get all projects, where the total cost of the project per month is greater than 70000. In the same result set
     * get all companies participating on such project along with cost of the project for the company.
     */
    public void executeComplexSelectScenario() {
        List<ProjectsWithCostsGreaterThanOutput> projectsWithCostsGreaterThan = repository.getProjectsWithCostsGreaterThan(70000);

        // check some post conditions
        assert projectsWithCostsGreaterThan != null;
        assert projectsWithCostsGreaterThan.size() == 2;
        assert projectsWithCostsGreaterThan.get(0).getCompanyName().equals("CleverGang");
        assert projectsWithCostsGreaterThan.get(0).getCompanyCost().equals(new BigDecimal("72000.00"));
        assert projectsWithCostsGreaterThan.get(1).getCompanyName().equals("Supersoft");
        assert projectsWithCostsGreaterThan.get(1).getCompanyCost().equals(new BigDecimal("13000.00"));
        logger.info("executeComplexSelectScenario output: {}", projectsWithCostsGreaterThan);
    }

    /**
     * 10. Call stored procedure/function and process results
     */
    public void callStoredProcedureScenario() {
        RegisterEmployeeOutput output = repository.callRegisterEmployee("Bretislav", "Wajtr", "bretislav.wajtr@test.com", new BigDecimal(40000), "MyDepartment", "MyCompany");

        // check some post conditions
        assert output != null;
        assert output.getEmployeePid() != null;
        assert output.getEmployeePid() > 10;
        assert output.getDepartmentPid() != null;
        assert output.getDepartmentPid() > 7;
        assert output.getCompanyPid() != null;
        assert output.getCompanyPid() > 3;
        logger.info("callStoredProcedureScenario output: {}", output);
    }


    /**
     * 11. Execute query using JDBC simple Statement (not PreparedStatement)
     * <br/>
     * Motivation why we need the "static statement" feature: In 96% of the cases, you’re better off writing
     * a PreparedStatement rather than a static statement - it's safer (sql injection),
     * easier (complex data types like dates) and sometimes faster (prepared statements reuse). However, there are
     * edge cases for complex queries and lot of data where it's actually faster to use simple statement query, because
     * your database’s cost-based optimiser or planner obtains some heads-up about what kind of data is really going to
     * be affected by the query and can therefore execute the query faster.
     * <p>
     * Good SQL API framework should offer way how to execute simple static statements.
     */
    public void executeSimpleStaticStatementScenario() {
        Company output = repository.findCompanyUsingSimpleStaticStatement(1);

        // check some post conditions
        assert output != null;
        assert output.getName().equals("CleverGang");
        logger.info("Output of scenario 11: {}", output);
    }
}




