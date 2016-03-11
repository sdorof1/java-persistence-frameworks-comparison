package com.clevergang.dbtests;

import com.clevergang.dbtests.repository.impl.jdbctemplate.JDBCDataRepositoryImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = DbTestsApplication.class)
@Transactional
@Rollback
public class JdbcTemplateScenariosTest {

    @Autowired
    private JDBCDataRepositoryImpl jdbcRepository;

    protected Scenarios scenarios;

    @Before
    public void setup() {
        scenarios = new Scenarios(jdbcRepository);
    }

    @Test
    public void scenarioOne() {
        scenarios.scenarioOne();
    }

    @Test
    public void scenarioTwo() {
        scenarios.scenarioTwo();
    }

    @Test
    public void scenarioThree() {
        scenarios.scenarioThree();
    }

    @Test
    public void scenarioFour() {
        scenarios.scenarioFour();
    }

    @Test
    public void scenarioFive() {
        scenarios.scenarioFive();
    }

    @Test
    public void scenarioSix() {
        scenarios.scenarioSix();
    }

    @Test
    public void scenarioSeven() {
        scenarios.scenarioSeven();
    }

    @Test
    public void scenarioEight() {
        scenarios.scenarioEight();
    }
}
