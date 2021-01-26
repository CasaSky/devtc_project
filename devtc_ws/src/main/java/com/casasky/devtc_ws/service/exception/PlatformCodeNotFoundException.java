package com.casasky.devtc_ws.service.exception;


import static java.lang.String.format;


public class PlatformCodeNotFoundException extends GlobalRuntimeException {

    private static final long serialVersionUID = 6844003896039668085L;


    public PlatformCodeNotFoundException(String name) {
        super(format("Platform with code %s not found", name));
    }

}
