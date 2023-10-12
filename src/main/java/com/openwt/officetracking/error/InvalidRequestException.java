package com.openwt.officetracking.error;

import org.springframework.http.HttpStatus;

public class InvalidRequestException extends DomainException {

    public InvalidRequestException(final String message, final Object... args) {
        super(HttpStatus.BAD_REQUEST, message, args);
    }
}
