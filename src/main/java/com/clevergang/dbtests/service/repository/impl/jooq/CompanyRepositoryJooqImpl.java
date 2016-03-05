package com.clevergang.dbtests.service.repository.impl.jooq;

import com.clevergang.dbtests.model.Company;
import com.clevergang.dbtests.service.repository.CompanyRepository;
import com.clevergang.dbtests.service.repository.impl.ImplBasedOn;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static com.clevergang.dbtests.service.repository.impl.RepoImplementation.JOOQ;
import static com.clevergang.dbtests.service.repository.impl.jooq.generated.Tables.COMPANY;


/**
 * @author Bretislav Wajtr <bretislav.wajtr@clevergang.com>
 */
@Repository
@ImplBasedOn(JOOQ)
public class CompanyRepositoryJooqImpl implements CompanyRepository {
    private static final Logger logger = LoggerFactory.getLogger(CompanyRepositoryJooqImpl.class);

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    private DSLContext create;

    @Override
    public Company find(Integer pid) {
        logger.info("Finding Company by ID using JOOQ");

        Company company = create.
            selectFrom(COMPANY)
            .where(COMPANY.PID.eq(pid))
            .fetchOneInto(Company.class);

        logger.info("Found company: " + company);

        return company;
    }
}
