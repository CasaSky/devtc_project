package com.casasky.devtc_ws.service;


import static java.lang.String.format;


class ToolNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 7415557910443800657L;


    public ToolNotFoundException(Long toolId) {
        super(format("Tool with id %s not found", toolId));
    }

}
