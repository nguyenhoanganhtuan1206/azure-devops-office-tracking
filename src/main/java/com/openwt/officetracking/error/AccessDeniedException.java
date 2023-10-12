package com.openwt.officetracking.error;

import org.springframework.http.HttpStatus;

public class AccessDeniedException extends DomainException {

    public AccessDeniedException(final String message, final Object... args) {
        super(HttpStatus.FORBIDDEN, message, args);
    }
}
