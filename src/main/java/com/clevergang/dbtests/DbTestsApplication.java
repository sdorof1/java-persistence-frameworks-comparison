package com.clevergang.dbtests;

import io.ebean.EbeanServer;
import io.ebean.EbeanServerFactory;
import io.ebean.config.ServerConfig;
import io.ebean.spring.txn.SpringJdbcTransactionManager;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.jooq.conf.RenderNameStyle;
import org.jooq.conf.Settings;
import org.jooq.conf.StatementType;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.logging.SLF4JLog;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

import javax.sql.DataSource;

/**
 * This whole thing is based on Spring Boot as we plan to use these frameworks with SpringBoot
 */
@SpringBootApplication
@Configuration
public class DbTestsApplication {

    /*
     * JOOQ CONFIGURATIONS
     */

    @Bean
    @Primary
    public Settings jooqSettings() {
        Settings ret = new Settings();
        ret.withRenderSchema(false);
        ret.setRenderFormatted(true);

        ret.setRenderNameStyle(RenderNameStyle.AS_IS);
        return ret;
    }

    @Bean
    @Qualifier("static-statement-jooq-settings")
    public Settings jooqStaticStatementSettings() {
        Settings ret = jooqSettings();
        ret.withStatementType(StatementType.STATIC_STATEMENT);
        return ret;
    }

    /*
     * MYBATIS CONFIGURATIONS
     */

    @Bean
    @Primary
    @SuppressWarnings("SpringJavaAutowiringInspection")
    public SqlSession myBatisDefaultSession(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean
    @Qualifier("batch-operations")
    @SuppressWarnings("SpringJavaAutowiringInspection")
    public SqlSession myBatisBatchOperationsSession(DataSource dataSource) throws Exception {
        /*
            NOTE: Unfortunately, in MyBatis it's not possible to execute batch and non-batch operations in single SqlSession.
            To support this scenario, we have to create completely new SqlSessionFactoryBean and completely new
            SqlSession. Surprisingly, this does not necessarily mean that the batch and non-batch operations will be
            executed in different transactions (as we would expect) - we tested this configuration using scenario 8.
            and it turned out that the bot non-batch and batch operations were run using same connection and in same transaction.
            I guess this has something to do with how connection is obtained by MyBatis from dataSource...
        */

        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setConfigLocation(new ClassPathResource("mybatis/mybatis-config.xml"));

        return new SqlSessionTemplate(sqlSessionFactoryBean.getObject(), ExecutorType.BATCH);
    }

    /*
     * EBEAN CONFIGURATIONS
     */

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Bean
    public ServerConfig ebeanServerConfig(DataSource dataSource) {
        ServerConfig config = new ServerConfig();
        config.setName("ebeanServer");
        config.setDefaultServer(true);
        config.setDataSource(dataSource);
        config.addPackage("com.clevergang.dbtests.repository.impl.ebean.entities");
        config.setExternalTransactionManager(new SpringJdbcTransactionManager());
        config.setAutoCommitMode(false);
        config.setExpressionNativeIlike(true);

        return config;
    }

    @Bean
    public EbeanServer ebeanServer(ServerConfig serverConfig) {
        return EbeanServerFactory.create(serverConfig);
    }

    /*
     * JDBI Configurations
     */

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Bean
    public DBI jdbiFactory(DataSource dataSource) {
        // note that for JDBI we have to wrap datasource with TransactionAwareDataSourceProxy otherwise JDBI won't respect
        // transactions created by spring
        TransactionAwareDataSourceProxy transactionAwareDataSourceProxy = new TransactionAwareDataSourceProxy(dataSource);
        DBI dbi = new DBI(transactionAwareDataSourceProxy);
        dbi.setSQLLog(new SLF4JLog());  // to enable SLF4J logging
        return dbi;
    }

    public static void main(String[] args) {
        SpringApplication.run(DbTestsApplication.class, args);
    }
}
