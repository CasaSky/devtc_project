package com.casasky.devtc_ws.service.exception;


import static java.lang.String.format;


public class DuplicateMaintenanceException extends GlobalRuntimeException {

    private static final long serialVersionUID = -6125547315340135858L;


    public DuplicateMaintenanceException(String toolName) {
        super(format("The maintainer has already a maintenance with the same tool name %s - try with a new maintenance revision", toolName));
    }

}
