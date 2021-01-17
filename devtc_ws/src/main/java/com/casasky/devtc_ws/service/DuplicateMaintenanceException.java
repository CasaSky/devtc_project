package com.casasky.devtc_ws.service;


import static java.lang.String.format;


class DuplicateMaintenanceException extends RuntimeException {

    private static final long serialVersionUID = -6125547315340135858L;


    DuplicateMaintenanceException(Long toolId) {
        super(format("The maintainer has already a maintenance with the same tool id %s - try with a new maintenance revision", toolId));
    }

}
