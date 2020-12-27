package com.casasky.core.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
class SentryConfig {

    @Value("${git.commit.id.abbrev}")
    private String commitId;


    @Value("${git.branch}")
    private String branch;

    //@Value("${fg-test/spring.datasource.password}")
    //private String serviceDomain;

    @Bean
    SentryInitializer sentryInitializer(WebserviceProperties webserviceProperties) {

         return new SentryInitializer(new SentryData(webserviceProperties.getName(), commitId , branch));

    }

}
