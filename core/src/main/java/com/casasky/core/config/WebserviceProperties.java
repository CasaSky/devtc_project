package com.casasky.core.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Configuration
@ConfigurationProperties(prefix = "com.casasky.webservice")
public class WebserviceProperties {

    private String name;


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

}