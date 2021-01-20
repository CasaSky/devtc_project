package com.casasky.devtc_ws.service;


import static java.lang.String.format;


class MaintenanceNotFoundException extends RuntimeException {


    private static final long serialVersionUID = 8593388492147758877L;


    MaintenanceNotFoundException(Long id) {
        super(format("Maintenance with id %s not found", id));
    }

}
