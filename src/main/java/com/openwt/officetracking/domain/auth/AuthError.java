package com.openwt.officetracking.domain.auth;

import com.openwt.officetracking.error.AccessDeniedException;
import lombok.experimental.UtilityClass;

import java.util.function.Supplier;

@UtilityClass
public class AuthError {

    public static Supplier<AccessDeniedException> supplyUserLoginFailed(final String message) {
        return () -> new AccessDeniedException(message);
    }
}
