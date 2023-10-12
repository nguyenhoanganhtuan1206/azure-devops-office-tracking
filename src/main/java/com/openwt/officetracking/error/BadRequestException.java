package com.openwt.officetracking.error;

import org.springframework.http.HttpStatus;

public class BadRequestException extends DomainException {

    public BadRequestException(final String message, final Object... args) {
        super(HttpStatus.BAD_REQUEST, message, args);
    }
}
