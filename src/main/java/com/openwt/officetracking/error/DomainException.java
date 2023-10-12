package com.openwt.officetracking.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class DomainException extends RuntimeException {

    private final HttpStatus httpStatus;

    public DomainException(final HttpStatus httpStatus, final String message, final Object... args) {
        super(String.format(message, args));
        this.httpStatus = httpStatus;
    }
}
