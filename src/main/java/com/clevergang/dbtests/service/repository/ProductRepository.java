package com.clevergang.dbtests.service.repository;

import java.util.List;

import com.clevergang.dbtests.model.Product;

/**
 * @author Bretislav Wajtr <bretislav.wajtr@clevergang.com>
 */
public interface ProductRepository {

    Integer insert(Product product);

    List<Integer> insertAll(List<Product> products);
}
