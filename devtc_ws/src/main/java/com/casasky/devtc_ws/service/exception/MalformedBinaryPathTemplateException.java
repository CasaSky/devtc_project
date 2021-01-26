package com.casasky.devtc_ws.service.exception;


import static java.lang.String.format;


public class MalformedBinaryPathTemplateException extends GlobalRuntimeException {

    private static final long serialVersionUID = -7106369595000698868L;


    public MalformedBinaryPathTemplateException(String binaryPathTemplate) {
        super(format("Invalid binary path template %s", binaryPathTemplate));
    }

}
