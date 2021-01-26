package com.casasky.devtc_ws.service.exception;


import static java.lang.String.format;


public class MalformedDownloadUrlTemplateException extends GlobalRuntimeException {

    private static final long serialVersionUID = 3655151382296249464L;


    public MalformedDownloadUrlTemplateException(String downloadUrlTemplate) {
        super(format("Invalid download url template %s", downloadUrlTemplate));
    }

}
