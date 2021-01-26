package com.casasky.devtc_ws.service.exception;


import static java.lang.String.format;


public class ToolNotFoundException extends GlobalRuntimeException {

    private static final long serialVersionUID = 7415557910443800657L;


    ToolNotFoundException(Long id) {
        super(format("Tool with id %s not found", id));
    }


    public ToolNotFoundException(String name) {
        super(format("Tool with name %s not found", name));
    }

}
