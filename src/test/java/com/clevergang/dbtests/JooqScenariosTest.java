package com.clevergang.dbtests;

import com.clevergang.dbtests.repository.impl.jooq.JooqDataRepositoryImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = DbTestsApplication.class)
@Transactional
@Rollback
public class JooqScenariosTest {

    @Autowired
    private JooqDataRepositoryImpl jooqRepository;

    private Scenarios scenarios;

    @Before
    public void setup() {
        scenarios = new Scenarios(jooqRepository);
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

    @Test
    public void scenarioNine() {
        scenarios.scenarioNine();
    }
}

