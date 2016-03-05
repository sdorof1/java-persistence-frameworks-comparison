package com.clevergang.dbtests;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.clevergang.dbtests.model.Product;
import com.clevergang.dbtests.service.repository.CompanyRepository;
import com.clevergang.dbtests.service.repository.EmployeeRepository;
import com.clevergang.dbtests.service.repository.ProductRepository;
import com.clevergang.dbtests.service.repository.impl.ImplBasedOn;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.StopWatch;

import static com.clevergang.dbtests.service.repository.impl.RepoImplementation.JOOQ;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = DbTestsApplication.class)
public class JooqScenariosTest {

    private static final Logger logger = LoggerFactory.getLogger(JooqScenariosTest.class);

    @Autowired
    @ImplBasedOn(JOOQ)
    private CompanyRepository companyRepository;

    @Autowired
    @ImplBasedOn(JOOQ)
    private EmployeeRepository employeeRepository;

    @Autowired
    @ImplBasedOn(JOOQ)
    private ProductRepository pica;

    @Test
    public void scenarioOne() {
        /* 1. Load single entity based on primary key */
        Integer pid = 1;
        companyRepository.find(pid);
    }

    @Test
    public void scenarioTwo() {
        /* 2. Load list of entities based on condition */
        Integer minSalary = 2000;
        employeeRepository.employeesWithSalaryGreaterThan(minSalary);
    }

    @Test
    public void scenarioThree() {
        /* 3. Save new single entity and return primary key */
        Product product = new Product();
        product.setName("TestProduct");
        product.setDescription("Description of test product");
        product.setCategory_pid(1);
        product.setPrice(new BigDecimal(345));

        pica.insert(product);
    }

    @Test
    public void scenarioFour() {
        /* 4. Batch insert multiple entities of same type and return generated keys */
        // create a list of thousand products
        List<Product> products = new ArrayList<>();
        StopWatch watch = new StopWatch();
        watch.start();
        for (int i = 0; i < 1000; i++) {
            Product product = new Product();
            product.setName(RandomStringUtils.randomAlphabetic(10));
            product.setDescription(RandomStringUtils.randomAlphabetic(50));
            product.setCategory_pid(RandomUtils.nextInt(1, 3));
            product.setPrice(new BigDecimal(RandomUtils.nextInt(1, 1000)));
            products.add(product);
        }
        List<Integer> result = pica.insertAll(products);
        watch.stop();
        logger.info("Total time {} ms", watch.getTotalTimeMillis());
    }

    @Test
    public void scenarioFive() {
          /* 5.  Update single existing entity - update one field of the entity */
    }

    @Test
    public void scenarioSix() {

    }

    @Test
    public void scenarioSeven() {

    }

    @Test
    public void scenarioEight() {

    }
}
