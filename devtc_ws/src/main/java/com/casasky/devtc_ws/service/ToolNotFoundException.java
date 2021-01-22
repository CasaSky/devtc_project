package com.casasky.devtc_ws.service;


import static java.lang.String.format;


class ToolNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 7415557910443800657L;


    ToolNotFoundException(Long id) {
        super(format("Tool with id %s not found", id));
    }


    ToolNotFoundException(String name) {
        super(format("Tool with name %s not found", name));
    }

}
