package com.openwt.officetracking.domain.ability_result;

import com.openwt.officetracking.error.NotFoundException;
import lombok.experimental.UtilityClass;

import java.util.function.Supplier;

@UtilityClass
public class AbilityResultError {

    public static Supplier<NotFoundException> supplyAbilityResultNotFound(final String fieldName, final Object fieldValue) {
        return () -> new NotFoundException("Ability Result with %s %s not found", fieldName, fieldValue);
    }
}
