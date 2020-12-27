package com.casasky.core.util;


import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;


public class TimeUtil {

    private static final ZoneId DEFAULT_ZONE = ZoneId.of("Europe/Berlin");

    public static ZonedDateTime now() {

        // adjust jvm time units to postgres
        return ZonedDateTime.now()
                .withZoneSameInstant(DEFAULT_ZONE)
                .truncatedTo(ChronoUnit.MICROS);

    }

}
