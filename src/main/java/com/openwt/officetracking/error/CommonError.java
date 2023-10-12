package com.openwt.officetracking.error;

import lombok.experimental.UtilityClass;

import java.util.function.Supplier;

@UtilityClass
public class CommonError {

    public static Supplier<NotFoundException> supplyNotFoundError(final String message) {
        return () -> new NotFoundException(message);
    }

    public static Supplier<BadRequestException> supplyValidationError(final String message) {
        return () -> new BadRequestException(message);
    }

    public static Supplier<AccessDeniedException> supplyAccessDeniedError() {
        return () -> new AccessDeniedException("You do not have permission to access this resource");
    }

    public static Supplier<BadRequestException> supplyBadRequestError(final String message) {
        return () -> new BadRequestException(message);
    }
}
