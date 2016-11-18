package com.clevergang.dbtests;

import org.jooq.conf.RenderNameStyle;
import org.jooq.conf.Settings;
import org.jooq.conf.StatementType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * This whole thing is based on Spring Boot as we plan to use these frameworks with SpringBoot
 */
@SpringBootApplication
@Configuration
public class DbTestsApplication {

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

    public static void main(String[] args) {
        SpringApplication.run(DbTestsApplication.class, args);
    }
}
