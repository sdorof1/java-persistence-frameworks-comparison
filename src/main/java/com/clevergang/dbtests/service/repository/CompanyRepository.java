package com.clevergang.dbtests.service.repository;

import com.clevergang.dbtests.model.Company;

/**
 * @author Bretislav Wajtr <bretislav.wajtr@clevergang.com>
 */
public interface CompanyRepository {

    Company find(Integer pid);
}
