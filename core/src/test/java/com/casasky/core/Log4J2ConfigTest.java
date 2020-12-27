package com.casasky.core;


import static java.lang.String.format;

import org.apache.logging.log4j.LogManager;
import org.junit.jupiter.api.Test;


class Log4J2ConfigTest {

    @Test
    void testAndCheckLogger() {

        var loggerTestMessageFormat = "This is a check for the logger configuration XML file message at %s level.";
        var logger = LogManager.getLogger(getClass());
        logger.debug(format(loggerTestMessageFormat, "DEBUG"));
        logger.info(format(loggerTestMessageFormat, "INFO"));
        logger.error(format(loggerTestMessageFormat, "ERROR"));
        logger.fatal(format(loggerTestMessageFormat, "FATAL"));

    }

}
