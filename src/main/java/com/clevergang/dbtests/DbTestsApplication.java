package com.clevergang.dbtests;

import org.jooq.conf.RenderNameStyle;
import org.jooq.conf.Settings;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@Configuration
public class DbTestsApplication {

    @Bean
    public Settings jooqSettings() {
        Settings ret = new Settings();
        ret.withRenderSchema(false);
        ret.setRenderFormatted(true);
        ret.setRenderNameStyle(RenderNameStyle.AS_IS);
        return ret;
    }

	public static void main(String[] args) {
		SpringApplication.run(DbTestsApplication.class, args);
	}
}
