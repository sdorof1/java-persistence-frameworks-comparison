package com.clevergang.dbtests.service.repository.impl.jooq;

import java.util.List;

import com.clevergang.dbtests.model.Product;
import com.clevergang.dbtests.service.repository.ProductRepository;
import com.clevergang.dbtests.service.repository.impl.ImplBasedOn;
import org.jooq.BatchBindStep;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static com.clevergang.dbtests.service.repository.impl.RepoImplementation.JOOQ;
import static com.clevergang.dbtests.service.repository.impl.jooq.generated.Tables.PRODUCT;

/**
 * @author Bretislav Wajtr <bretislav.wajtr@clevergang.com>
 */
@Repository
@ImplBasedOn(JOOQ)
public class ProductRepositoryJooqImpl implements ProductRepository {

    private static final Logger logger = LoggerFactory.getLogger(CompanyRepositoryJooqImpl.class);

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    private DSLContext create;

    @Override
    public Integer insert(Product product) {
        logger.info("Inserting product using JOOQ");

        return create
            .insertInto(PRODUCT)
            .set(PRODUCT.PRODUCT_CATEGORY_PID, product.getCategory_pid())
            .set(PRODUCT.NAME, product.getName())
            .set(PRODUCT.DESCRIPTION, product.getDescription())
            .set(PRODUCT.PRICE, product.getPrice())
            .returning(PRODUCT.PID)
            .fetchOne()
            .getPid();
    }

    @Override
    public List<Integer> insertAll(List<Product> products) {
        logger.info("Batch inserting products using JOOQ");

        // FIXME This is weird syntax (especially those null dummy values) but works and showing same speed as JDBCTemplate
        // prepare batch statement
        BatchBindStep batch = create.batch(
            create
                .insertInto(PRODUCT,
                    PRODUCT.PRODUCT_CATEGORY_PID,
                    PRODUCT.NAME,
                    PRODUCT.DESCRIPTION,
                    PRODUCT.PRICE)
                .values((Integer) null, null, null, null));

        // bind values
        for (Product product : products) {
            batch.bind(
                product.getCategory_pid(),
                product.getName(),
                product.getDescription(),
                product.getPrice()
            );
        }

        // execute batch
        batch.execute();

        // FIXME Not even JOOQ can easily return newly generated ID's from batch operations
        return null;
    }


}
