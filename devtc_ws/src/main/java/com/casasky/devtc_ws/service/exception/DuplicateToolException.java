package com.casasky.devtc_ws.service.exception;


import static java.lang.String.format;


public class DuplicateToolException extends GlobalRuntimeException {


    private static final long serialVersionUID = -8636747380078393273L;


    public DuplicateToolException(String toolName) {
        super(format("Tool with name %s already exists", toolName));
    }

}
