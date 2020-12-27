package com.casasky.core.config;


import javax.annotation.PostConstruct;

import io.sentry.Sentry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.Strings;


public class SentryInitializer {

    private static final Logger LOG = LogManager.getLogger();

    private final SentryData sentryData;

    public SentryInitializer(SentryData sentryData) {
        this.sentryData = sentryData;
    }


    @PostConstruct
    private void init() {

        String dsn = System.getenv("log.sentry.dsn");

        if (Strings.isNotBlank(dsn)) {

            Sentry.init(options -> {
                options.setDsn(dsn);
                options.setEnableExternalConfiguration(true);
                options.setRelease(sentryData.release);
            });

            Sentry.configureScope(scope -> {
                scope.setTag("webservice", sentryData.webservice);
                scope.setTag("branch", sentryData.branch);
            });

            LOG.info("sentry initialized");

        }
        else {

            LOG.info("no sentry dsn is configured - running without sentry");

        }

    }

}
