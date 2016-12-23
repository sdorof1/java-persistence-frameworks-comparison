package com.clevergang.dbtests;

import com.clevergang.dbtests.repository.impl.mybatis.MyBatisDataRepositoryImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Bretislav Wajtr
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = DbTestsApplication.class)
@Transactional
@Rollback
public class MyBatisScenariosTest {

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    private MyBatisDataRepositoryImpl myBatisDataRepository;

    private Scenarios scenarios;

    @Before
    public void setup() {
        scenarios = new Scenarios(myBatisDataRepository);
    }


    @Test
    public void scenarioOne() {
        scenarios.fetchSingleEntityScenario(1);
    }

    @Test
    public void scenarioTwo() {
        scenarios.fetchListOfEntitiesScenario(25000);
    }

    @Test
    public void scenarioThree() {
        scenarios.saveNewEntityScenario();
    }

    @Test
    public void scenarioFour() {
        scenarios.batchInsertMultipleEntitiesScenario();
    }

    @Test
    public void scenarioFive() {
        scenarios.updateCompleteEntityScenario();
    }

    @Test
    public void scenarioSix() {
        scenarios.fetchManyToOneRelationScenario();
    }

    @Test
    public void scenarioSeven() {
        scenarios.fetchOneToManyRelationScenario();
    }

    @Test
    public void scenarioEight() {
        scenarios.updateCompleteOneToManyRelationScenario();
    }

    @Test
    public void scenarioNine() {
        scenarios.executeComplexSelectScenario();
    }

    @Test
    public void scenarioTen() {
        scenarios.callStoredProcedureScenario();
    }

    @Test
    public void scenarioEleven() {
        scenarios.executeSimpleStaticStatementScenario();
    }

    @Test
    public void scenarioTwelve() {
        scenarios.removeSingleEntityScenario();
    }

}
