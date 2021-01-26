package com.casasky.devtc_ws.service.exception;


import static java.lang.String.format;


public class MaintenanceNotFoundException extends GlobalRuntimeException {


    private static final long serialVersionUID = 8593388492147758877L;


    public MaintenanceNotFoundException(Long id) {
        super(format("Maintenance with id %s not found", id));
    }

    public MaintenanceNotFoundException(String toolName, String platform) {
        super(format("Maintenance for %s in %s not found", toolName, platform));
    }

}
