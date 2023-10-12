package com.openwt.officetracking.error;

import org.springframework.http.HttpStatus;

public class NotFoundException extends DomainException {

    public NotFoundException(final String message, final Object... args) {
        super(HttpStatus.NOT_FOUND, message, args);
    }
}
