package com.clevergang.dbtests.service.repository.impl.jdbctemplate;

import java.util.List;

import com.clevergang.dbtests.model.Product;
import com.clevergang.dbtests.service.repository.ProductRepository;
import com.clevergang.dbtests.service.repository.impl.ImplBasedOn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import static com.clevergang.dbtests.service.repository.impl.RepoImplementation.JDBCTEMPLATE;

/**
 * @author Bretislav Wajtr <bretislav.wajtr@clevergang.com>
 */
@Repository
@ImplBasedOn(JDBCTEMPLATE)
public class ProductRepositoryJDBCImpl implements ProductRepository {

    private static final Logger logger = LoggerFactory.getLogger(CompanyRepositoryJDBCImpl.class);

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public Integer insert(Product product) {
        logger.info("Inserting product using JDBCTemplate");

        String insertStatement = " INSERT INTO product (name, description, product_category_pid, price) " +
            " VALUES (:name, :description, :category_id, :price)";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", product.getName());
        params.addValue("description", product.getDescription());
        params.addValue("category_id", product.getCategory_pid());
        params.addValue("price", product.getPrice());

        KeyHolder generatedKey = new GeneratedKeyHolder();

        jdbcTemplate.update(insertStatement, params, generatedKey);

        return (Integer) generatedKey.getKeys().get("pid");
    }

    @Override
    public List<Integer> insertAll(List<Product> products) {
        logger.info("Batch inserting products using JDBCTemplate");

        String insertStatement = " INSERT INTO product (name, description, product_category_pid, price) " +
            " VALUES (:name, :description, :category_id, :price)";

        MapSqlParameterSource[] paramsList = products
            .stream()
            .map(product -> {
                MapSqlParameterSource params = new MapSqlParameterSource();
                params.addValue("name", product.getName());
                params.addValue("description", product.getDescription());
                params.addValue("category_id", product.getCategory_pid());
                params.addValue("price", product.getPrice());
                return params;
            })
            .toArray(MapSqlParameterSource[]::new);

        jdbcTemplate.batchUpdate(insertStatement, paramsList);

        // FIXME JDBCTemplate cannot return IDs for batch update!!
        return null;
    }

}
