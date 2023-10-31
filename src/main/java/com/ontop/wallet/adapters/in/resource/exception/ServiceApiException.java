package com.ontop.wallet.adapters.in.resource.exception;

import org.springframework.http.HttpStatus;

public class ServiceApiException extends RuntimeException {
    private final HttpStatus httpStatus;

    public ServiceApiException(HttpStatus httpStatus, String msg) {
        super(msg);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

}
