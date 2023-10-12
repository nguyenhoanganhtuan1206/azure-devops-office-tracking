package com.openwt.officetracking.domain.user;

import com.openwt.officetracking.error.BadRequestException;
import com.openwt.officetracking.error.NotFoundException;
import lombok.experimental.UtilityClass;

import java.util.function.Supplier;

@UtilityClass
public class UserError {

    public static Supplier<NotFoundException> supplyUserNotFound(final String fieldName, final Object fieldValue) {
        return () -> new NotFoundException("User with %s %s not found", fieldName, fieldValue);
    }

    public static Supplier<BadRequestException> supplyUserExisted(final String fieldName, final Object fieldValue) {
        return () -> new BadRequestException("User with %s %s has been taken", fieldName, fieldValue);
    }

    public static Supplier<BadRequestException> supplierPasswordNotMatches() {
        return () -> new BadRequestException("Current password is not correct");
    }

    public static Supplier<BadRequestException> supplierLockUserAccount(final String message) {
        return () -> new BadRequestException(message);
    }

    public static Supplier<BadRequestException> supplierCodeExpiration(final String message) {
        return () -> new BadRequestException(message);
    }

    public static Supplier<BadRequestException> supplyInvalidResetPasswordCode(final String message) {
        return () -> new BadRequestException(message);
    }
}
