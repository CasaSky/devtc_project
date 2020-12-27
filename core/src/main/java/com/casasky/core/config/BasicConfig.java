package com.casasky.core.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

// aggregating needed basic configurations
@Import({ SentryConfig.class, WebserviceProperties.class, GitConfig.class })
@Configuration
public class BasicConfig {
}
