package com.clevergang.dbtests.service.repository.impl.jooq;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

import com.clevergang.dbtests.model.Company;
import com.clevergang.dbtests.model.Employee;
import com.clevergang.dbtests.model.Project;
import com.clevergang.dbtests.service.repository.DataRepository;
import com.clevergang.dbtests.service.repository.impl.ImplBasedOn;
import org.jooq.BatchBindStep;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static com.clevergang.dbtests.service.repository.impl.RepoImplementation.JOOQ;
import static com.clevergang.dbtests.service.repository.impl.jooq.generated.Tables.*;


/**
 * @author Bretislav Wajtr <bretislav.wajtr@clevergang.com>
 */
@Repository
@ImplBasedOn(JOOQ)
public class JooqDataRepositoryImpl implements DataRepository {
    private static final Logger logger = LoggerFactory.getLogger(JooqDataRepositoryImpl.class);

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    private DSLContext create;

    @Override
    public Company findCompany(Integer pid) {
        logger.info("Finding Company by ID using JOOQ");

        Company company = create.
            selectFrom(COMPANY)
            .where(COMPANY.PID.eq(pid))
            .fetchOneInto(Company.class);

        logger.info("Found company: " + company);

        return company;
    }

    @Override
    public List<Employee> employeesWithSalaryGreaterThan(Integer minSalary) {
        logger.info("Looking for employeesWithSalaryGreaterThan using JOOQ");

        return create
            .select()
            .from(EMPLOYEE)
            .where(EMPLOYEE.SALARY.greaterThan(new BigDecimal(minSalary)))
            .fetchInto(Employee.class);
    }

    @Override
    public Integer insertProject(Project project) {
        logger.info("Inserting project using JOOQ");

        return create
            .insertInto(PROJECT)
            .set(PROJECT.NAME, project.getName())
            .set(PROJECT.DATESTARTED, Date.valueOf(project.getDate()))
            .returning(PROJECT.PID)
            .fetchOne()
            .getPid();
    }

    @Override
    public List<Integer> insertAllProjects(List<Project> projects) {
        logger.info("Batch inserting projects using JOOQ");

        // FIXME This is weird syntax (especially those null dummy values) but works and showing same speed as JDBCTemplate
        // prepare batch statement
        BatchBindStep batch = create.batch(
            create
                .insertInto(PROJECT,
                    PROJECT.NAME,
                    PROJECT.DATESTARTED)
                .values((String) null, null));

        // bind values
        for (Project project : projects) {
            batch.bind(
                project.getName(),
                Date.valueOf(project.getDate())
            );
        }

        // execute batch
        batch.execute();

        // FIXME Not even JOOQ can easily return newly generated ID's from batch operations
        return null;
    }

}

