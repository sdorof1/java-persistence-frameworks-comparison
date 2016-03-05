package com.clevergang.dbtests;

import com.clevergang.dbtests.service.repository.CompanyRepository;
import com.clevergang.dbtests.service.repository.EmployeeRepository;
import com.clevergang.dbtests.service.repository.ProductRepository;
import org.springframework.stereotype.Component;

/**
 * @author Bretislav Wajtr <bretislav.wajtr@clevergang.com>
 */
@Component
public class Scenarios {
    private CompanyRepository companyRepository;
    private EmployeeRepository employeeRepository;
    private ProductRepository productRepository;



}


