package com.openwt.officetracking.domain.auth;

import lombok.experimental.UtilityClass;

import static com.openwt.officetracking.error.CommonError.supplyValidationError;
import static org.apache.commons.lang3.StringUtils.isBlank;

@UtilityClass
public class RegisterMobileValidation {

    public static void validateUserRegisterMobileEmail(final String email) {
        if (isBlank(email)) {
            throw supplyValidationError("Email cannot be blank").get();
        }
    }
}
