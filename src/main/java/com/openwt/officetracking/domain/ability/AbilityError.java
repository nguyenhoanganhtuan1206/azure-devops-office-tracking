package com.openwt.officetracking.domain.ability;

import com.openwt.officetracking.error.NotFoundException;
import lombok.experimental.UtilityClass;

import java.util.function.Supplier;

@UtilityClass
public class AbilityError {

    public static Supplier<NotFoundException> supplyAbilityNotFound(final String fieldName, final Object fieldValue) {
        return () -> new NotFoundException("Ability with %s %s not found", fieldName, fieldValue);
    }
}
