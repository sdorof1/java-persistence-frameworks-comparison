package com.clevergang.dbtests;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.EbeanServerFactory;
import com.avaje.ebean.config.ServerConfig;
import com.avaje.ebean.springsupport.txn.SpringAwareJdbcTransactionManager;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.jooq.conf.RenderNameStyle;
import org.jooq.conf.Settings;
import org.jooq.conf.StatementType;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;

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
    public SqlSession myBatisBatchOperationstSession(DataSource dataSource) throws Exception {
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
        config.setExternalTransactionManager(new SpringAwareJdbcTransactionManager());
        config.setAutoCommitMode(false);
        config.setExpressionNativeIlike(true);

        return config;
    }

    @Bean
    public EbeanServer ebeanServer(ServerConfig serverConfig) {
        return EbeanServerFactory.create(serverConfig);
    }

    public static void main(String[] args) {
        SpringApplication.run(DbTestsApplication.class, args);
    }
}
