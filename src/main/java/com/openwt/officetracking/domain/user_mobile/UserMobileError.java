package com.openwt.officetracking.domain.user_mobile;

import com.openwt.officetracking.error.NotFoundException;
import lombok.experimental.UtilityClass;

import java.util.function.Supplier;

@UtilityClass
public class UserMobileError {

    public static Supplier<NotFoundException> supplyUserMobileNotFound(final String fieldName, final Object fieldValue) {
        return () -> new NotFoundException("Mobile with %s %s not found", fieldName, fieldValue);
    }
}
