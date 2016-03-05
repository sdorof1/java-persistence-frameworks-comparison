package com.clevergang.dbtests.service.repository.impl.jdbctemplate;

import java.util.HashMap;
import java.util.Map;

import com.clevergang.dbtests.model.Company;
import com.clevergang.dbtests.service.repository.CompanyRepository;
import com.clevergang.dbtests.service.repository.impl.ImplBasedOn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import static com.clevergang.dbtests.service.repository.impl.RepoImplementation.JDBCTEMPLATE;

/**
 * @author Bretislav Wajtr <bretislav.wajtr@clevergang.com>
 */
@Repository
@ImplBasedOn(JDBCTEMPLATE)
public class CompanyRepositoryJDBCImpl implements CompanyRepository {
    private static final Logger logger = LoggerFactory.getLogger(CompanyRepositoryJDBCImpl.class);

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public Company find(Integer pid) {
        logger.info("Finding Company by ID using JDBCTemplate");

        String query = "SELECT pid, address, name " +
            "           FROM company " +
            "           WHERE pid = :pid";

        Map<String, Object> params = new HashMap<>();
        params.put("pid", pid);

        Company company = jdbcTemplate.queryForObject(query, params, new BeanPropertyRowMapper<>(Company.class));

        logger.info("Found company: " + company);

        return company;
    }
}
